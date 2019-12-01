package cn.wanru.rpc.registry.zookeeper;

import cn.wanru.rpc.registry.Instance;
import cn.wanru.rpc.registry.ServiceRegistry;
import com.google.common.base.Strings;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceInstance;

import static cn.wanru.rpc.registry.Constants.STATUS_OUT_OF_SERVICE;
import static cn.wanru.rpc.registry.Constants.STATUS_UP;
import static cn.wanru.rpc.registry.zookeeper.ServiceInstanceUtils.fromInstance;
import static cn.wanru.rpc.util.ExceptionUtils.rethrowRuntimeException;

/**
 * @author xxf
 * @since 2019/11/17
 */
public class ZookeeperServiceRegistry implements ServiceRegistry {

    private ServiceDiscovery<Instance> serviceDiscovery;

    public ZookeeperServiceRegistry(ServiceDiscovery<Instance> serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public ServiceDiscovery<Instance> getServiceDiscovery() {
        return serviceDiscovery;
    }

    @Override
    public void register(Instance instance) {
        try {
            getServiceDiscovery().registerService(fromInstance(instance));
        } catch (Exception e) {
            rethrowRuntimeException(e);
        }
    }

    @Override
    public void deregister(Instance instance) {
        try {
            getServiceDiscovery().unregisterService(fromInstance(instance));
        } catch (Exception e) {
            rethrowRuntimeException(e);
        }
    }

    @Override
    public void setStatus(Instance instance, String status) {
        try {
            instance.setStatus(status);
            getServiceDiscovery().updateService(fromInstance(instance));
        } catch (Exception e) {
            rethrowRuntimeException(e);
        }
    }

    @Override
    public String getStatus(Instance instance) {
        try {
            ServiceInstance<Instance> serviceInstance = getServiceDiscovery()
                .queryForInstance(instance.getServiceName(), instance.getId());

            String status = serviceInstance.getPayload().getStatus();
            if (Strings.isNullOrEmpty(status)) {
                status = STATUS_UP;
            }
            return status;
        } catch (Exception e) {
            rethrowRuntimeException(e);
        }
        return STATUS_OUT_OF_SERVICE;
    }

    @Override
    public void close() {
        try {
            getServiceDiscovery().close();
        } catch (Exception e) {
            rethrowRuntimeException(e);
        }
    }
}
