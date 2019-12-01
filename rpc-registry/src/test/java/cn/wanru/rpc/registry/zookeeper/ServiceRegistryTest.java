package cn.wanru.rpc.registry.zookeeper;

import cn.wanru.rpc.registry.Instance;
import cn.wanru.rpc.registry.ServiceRegistry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.hamcrest.core.IsEqual;
import org.junit.Test;

import static cn.wanru.rpc.registry.Constants.STATUS_UP;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author xxf
 * @since 2019/11/17
 */
public class ServiceRegistryTest {

    @Test
    public void testRegistryService() throws Exception {
        ZookeeperRegistryCenterConfig config = new ZookeeperRegistryCenterConfig();
        ServiceDiscovery<Instance> serviceDiscovery = ZkUtils.createServiceDiscovery(config);

        ServiceRegistry registry = new ZookeeperServiceRegistry(serviceDiscovery);

        Instance instance = Instance.newBuidler()
            .serviceName("cn.wanru.im.gim")
            .port(10000)
            .build();

        registry.register(instance);

        String status = registry.getStatus(instance);
        assertThat(status, IsEqual.equalTo(STATUS_UP));
    }

    @Test
    public void testRegistryMultiService() throws Exception {
        ZookeeperRegistryCenterConfig config = new ZookeeperRegistryCenterConfig();
        ServiceDiscovery<Instance> serviceDiscovery = ZkUtils.createServiceDiscovery(config);

        ServiceRegistry registry = new ZookeeperServiceRegistry(serviceDiscovery);

        Instance instance = Instance.newBuidler()
            .serviceName("cn.wanru.im.gim")
            .port(10000)
            .build();

        registry.register(instance);

        instance = Instance.newBuidler()
            .serviceName("cn.wanru.im.gim")
            .port(10001)
            .build();
        registry.register(instance);
    }

}
