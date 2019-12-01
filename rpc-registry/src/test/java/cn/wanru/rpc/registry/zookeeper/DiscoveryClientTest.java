package cn.wanru.rpc.registry.zookeeper;

import cn.wanru.rpc.registry.Constants;
import cn.wanru.rpc.registry.DiscoveryClient;
import cn.wanru.rpc.registry.Instance;
import cn.wanru.rpc.registry.InstanceChangeListener;
import cn.wanru.rpc.registry.InstanceFilter;
import com.google.common.collect.Lists;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.junit.Test;

import java.util.Arrays;
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
        client.addInstanceFilter(AndFilter.and(new GroupFilter(""), StatusUpFilter.getInstance()));
        client.addListener(new DefaultListener());
        client.watchServiceName("cn.wanru.im.gim");
        List<Instance> services = client.getServices();
        System.out.println(Arrays.toString(services.toArray()));

        System.in.read();
    }

    static class AndFilter implements InstanceFilter {

        private List<InstanceFilter> list;

        AndFilter(InstanceFilter... filters) {
            this.list = Lists.newArrayList(filters);
        }

        @Override
        public boolean test(Instance instance) {
            for (InstanceFilter filter : list) {
                if (!filter.test(instance)) {
                    return false;
                }
            }
            return true;
        }

        static InstanceFilter and(InstanceFilter... filters) {
            return new AndFilter(filters);
        }
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

    static class StatusUpFilter implements InstanceFilter {

        static final StatusUpFilter inst = new StatusUpFilter();

        private StatusUpFilter() {
        }

        static StatusUpFilter getInstance() {
            return inst;
        }

        @Override
        public boolean test(Instance instance) {
            return Constants.STATUS_UP.equalsIgnoreCase(instance.getStatus());
        }
    }

    static class DefaultListener implements InstanceChangeListener {

        @Override
        public void onChanged(String serviceName, List<Instance> instances) {
            System.out.println("Services changed, run thread is " + Thread.currentThread());
            for (Instance instance : instances) {
                System.out.println(instance);
            }
        }
    }

}
