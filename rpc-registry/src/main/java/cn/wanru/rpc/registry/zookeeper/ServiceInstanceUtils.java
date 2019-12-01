package cn.wanru.rpc.registry.zookeeper;

import cn.wanru.rpc.registry.Instance;
import cn.wanru.rpc.util.ExceptionUtils;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.ServiceType;

import static cn.wanru.rpc.registry.Constants.STATUS_UP;

/**
 * @author xxf
 * @since 2019/11/17
 */
public abstract class ServiceInstanceUtils {

    public static ServiceInstance<Instance> fromInstance(Instance instance) {
        try {
            return ServiceInstance.<Instance>builder()
                .id(instance.getIp() + ":" + instance.getPort())
                .name(instance.getServiceName())
                .serviceType(ServiceType.DYNAMIC)
                .enabled(STATUS_UP.equals(instance.getStatus()))
                .port(instance.getPort())
                .address(instance.getIp())
                .payload(instance)
                .build();
        } catch (Exception e) {
            ExceptionUtils.rethrowRuntimeException(e);
        }
        return null;
    }

}
