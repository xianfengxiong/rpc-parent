package cn.wanru.rpc.registry.zookeeper;

import cn.wanru.rpc.registry.DiscoveryClient;
import cn.wanru.rpc.registry.Instance;
import cn.wanru.rpc.registry.InstanceChangeListener;
import cn.wanru.rpc.registry.InstanceFilter;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.junit.Test;

import java.util.List;

/**
 * @author xxf
 * @since 2019/12/1
 */
public class DiscoveryClientTest {

    private ServiceDiscovery<Instance> serviceDiscovery;

    @Test
    public void testDiscoveryService() throws Exception {
        ZookeeperRegistryCenterConfig config = new ZookeeperRegistryCenterConfig();
        serviceDiscovery = ZkUtils.createServiceDiscovery(config);
        DiscoveryClient client = new ZookeeperDiscoveryClient(serviceDiscovery);
        client.addInstanceFilter(new GroupFilter(""));
        client.addListener(new DefaultListener());
        client.watchServiceName("cn.wanru.im.gim");
        List<Instance> services = client.getServices();
    }

    static class GroupFilter implements InstanceFilter {

        private String targetGroup;

        GroupFilter(String targetGroup) {
            this.targetGroup = targetGroup;
        }

        @Override
        public boolean test(Instance instance) {
            return targetGroup.equalsIgnoreCase(instance.getGroup());
        }
    }

    static class DefaultListener implements InstanceChangeListener {

        @Override
        public void onChanged(String serviceName, List<Instance> instances) {
            System.out.println(Thread.currentThread());
            for (Instance instance : instances) {
                System.out.println(instance);
            }
        }
    }

}
