package cn.wanru.rpc.registry.zookeeper;

import cn.wanru.rpc.registry.DiscoveryClient;
import cn.wanru.rpc.registry.Instance;
import cn.wanru.rpc.registry.InstanceChangeListener;
import cn.wanru.rpc.registry.InstanceFilter;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.shaded.com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.curator.x.discovery.ServiceCache;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.ServiceCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static cn.wanru.rpc.util.ExceptionUtils.rethrowRuntimeException;

/**
 * @author xxf
 * @since 2019/11/17
 */
public class ZookeeperDiscoveryClient implements DiscoveryClient, ServiceCacheListener {

    private static final Logger LOG = LoggerFactory.getLogger(ZookeeperDiscoveryClient.class);

    private InstanceFilterSupport filterSupport = new InstanceFilterSupport();

    private ListenerSupport listenerSupport = new ListenerSupport();

    private ServiceDiscovery<Instance> serviceDiscovery;

    private ServiceCache<Instance> serviceCache;

    private String serviceName;

    private AtomicBoolean serviceNameWatching = new AtomicBoolean(false);

    public ZookeeperDiscoveryClient(ServiceDiscovery<Instance> serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    @Override
    public void watchServiceName(String serviceName) {
        if (serviceNameWatching.compareAndSet(false, true)) {
            try {
                this.serviceName = serviceName;

                ThreadFactory threadFactory =
                    new ThreadFactoryBuilder().setNameFormat("serviceCache").build();

                this.serviceCache = serviceDiscovery.serviceCacheBuilder()
                    .name(serviceName).threadFactory(threadFactory).build();
                this.serviceCache.addListener(this);
                this.serviceCache.start();
            } catch (Exception e) {
                rethrowRuntimeException(e);
            }
        } else {
            LOG.info("Already watching service name [{}], don't watching repeat", serviceName);
        }
    }

    @Override
    public void addInstanceFilter(InstanceFilter filter) {
        filterSupport.addInstanceFilter(filter);
    }

    @Override
    public void removeInstanceFilter(InstanceFilter filter) {
        filterSupport.removeInstanceFilter(filter);
    }

    @Override
    public void addListener(InstanceChangeListener listener) {
        listenerSupport.addListener(listener);
    }

    @Override
    public void removeListener(InstanceChangeListener listener) {
        listenerSupport.removeListener(listener);
    }

    @Override
    public List<Instance> getServices() {
        if (!serviceNameWatching.get()) {
            throw new IllegalStateException("Not watch service name, can not retrieve instances");
        }
        List<ServiceInstance<Instance>> serviceInstances = serviceCache.getInstances();
        List<Instance> instances = serviceInstances.stream()
            .map(ServiceInstance::getPayload).collect(Collectors.toList());
        return filterSupport.filter(instances);
    }

    @Override
    public void close() throws Exception {
        serviceCache.close();
    }

    @Override
    public void cacheChanged() {
        List<Instance> services = getServices();
        listenerSupport.fireInstanceChanged(serviceName, services);
    }

    @Override
    public void stateChanged(CuratorFramework client, ConnectionState newState) {
        cacheChanged();
    }
}
