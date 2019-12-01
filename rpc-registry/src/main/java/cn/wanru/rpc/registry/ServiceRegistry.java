package cn.wanru.rpc.registry;

/**
 * @author xxf
 * @since 2019/11/16
 */
public interface ServiceRegistry extends AutoCloseable {

    void register(Instance instance);

    void deregister(Instance instance);

    void setStatus(Instance instance, String status);

    String getStatus(Instance instance);

}
