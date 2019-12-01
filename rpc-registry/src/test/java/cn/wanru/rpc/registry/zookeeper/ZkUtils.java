package cn.wanru.rpc.registry.zookeeper;

import cn.wanru.rpc.registry.Instance;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;

/**
 * @author xxf
 * @since 2019/12/1
 */
public class ZkUtils {

    public static ServiceDiscovery<Instance> createServiceDiscovery(ZookeeperRegistryCenterConfig config) throws Exception {
        CuratorFramework client = CuratorFrameworkFactory.newClient(
            config.getConnectionString(), 1000, 1000, new RetryNTimes(3, 20));
        client.start();

        ServiceDiscovery<Instance> serviceDiscovery =
            ServiceDiscoveryBuilder.builder(Instance.class)
                .client(client).basePath(config.getBasePath()).build();
        serviceDiscovery.start();
        return serviceDiscovery;
    }
}
