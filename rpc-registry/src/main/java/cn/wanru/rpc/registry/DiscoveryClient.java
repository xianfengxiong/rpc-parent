package cn.wanru.rpc.registry;

import java.util.List;

/**
 * 服务发现组件，只能监听一个服务
 *
 * @author xxf
 * @since 2019/11/17
 */
public interface DiscoveryClient extends AutoCloseable {

    void addInstanceFilter(InstanceFilter filter);

    void removeInstanceFilter(InstanceFilter filter);

    void addListener(InstanceChangeListener listener);

    void removeListener(InstanceChangeListener listener);

    /**
     * 监听服务提供者的变化
     *
     * @param serviceName
     */
    void watchServiceName(String serviceName);

    List<Instance> getServices();

}
