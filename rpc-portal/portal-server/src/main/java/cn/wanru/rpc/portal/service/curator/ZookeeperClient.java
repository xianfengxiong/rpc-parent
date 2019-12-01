package cn.wanru.rpc.portal.service.curator;

import cn.wanru.rpc.portal.config.ZookeeperProperties;
import cn.wanru.rpc.portal.model.ServiceDetail;
import cn.wanru.rpc.registry.Instance;
import cn.wanru.rpc.registry.zookeeper.ServiceInstanceUtils;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceInstanceBuilder;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Component
public class ZookeeperClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperClient.class);

    private static final JsonInstanceSerializer<Instance> serializer
        = new JsonInstanceSerializer<Instance>(Instance.class);

    private static CuratorFramework client;

    private String basePath;

    private String connString;

    public ZookeeperClient(ZookeeperProperties zkProp) {
        this.basePath = zkProp.getBasePath();
        this.connString = zkProp.getConnectionString();
        try {
            ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
            client = CuratorFrameworkFactory.builder()
                .connectString(connString)
                .retryPolicy(retryPolicy)
                .connectionTimeoutMs(5 * 1000)
                .sessionTimeoutMs(5 * 1000)
                .build();
            client.start();
        } catch (Exception e) {
            LOGGER.error("ZookeeperClient[{}] init failed.", connString, e);
        }
    }

    public List<String> getAllService() throws Exception {
        List<String> names = client.getChildren().forPath(basePath);
        return ImmutableList.copyOf(names);
    }

    public List<Instance> getInstances(String serviceName) throws Exception {
        ImmutableList.Builder<Instance> builder = ImmutableList.builder();
        String path = pathForName(serviceName);
        List<String> instanceIds;

        try {
            instanceIds = client.getChildren().forPath(path);
        } catch (KeeperException.NoNodeException e) {
            instanceIds = Lists.newArrayList();
        }

        for (String id : instanceIds) {
            Instance inst = getInstance(serviceName, id);
            if (inst != null) {
                builder.add(inst);
            }
        }

        return builder.build();
    }

    private Instance getInstance(String serviceName, String id) throws Exception {
        String path = pathForInstance(serviceName, id);
        try {
            byte[] bytes = client.getData().forPath(path);
            ServiceInstance<Instance> serviceInstance = serializer.deserialize(bytes);
            return serviceInstance.getPayload();
        } catch (KeeperException.NoNodeException ignore) {
            // ignore
        }
        return null;
    }

    public Instance getInstance(String serviceName, String ip, int port) throws Exception {
        String id = getId(ip, port);
        return getInstance(serviceName, id);
    }

    public void updateInstance(Instance instance) throws Exception {
        ServiceInstance<Instance> serviceInstance = ServiceInstanceUtils.fromInstance(instance);
        byte[] bytes = serializer.serialize(serviceInstance);
        String path = pathForInstance(instance.getServiceName(), getId(instance.getIp(), instance.getPort()));
        client.setData().forPath(path, bytes);
    }

    private String pathForInstance(String name, String id) {
        return ZKPaths.makePath(pathForName(name), id);
    }

    private String pathForName(String name) {
        return ZKPaths.makePath(basePath, name);
    }

    private static String getId(String ip, int port) {
        return new StringBuilder(ip).append(":").append(port).toString();
    }

}
