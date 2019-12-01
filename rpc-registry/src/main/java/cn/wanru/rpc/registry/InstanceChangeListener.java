package cn.wanru.rpc.registry;

import java.util.List;

/**
 * @author xxf
 * @since 2019/11/17
 */
public interface InstanceChangeListener {

    void onChanged(String serviceName, List<Instance> instances);

}
