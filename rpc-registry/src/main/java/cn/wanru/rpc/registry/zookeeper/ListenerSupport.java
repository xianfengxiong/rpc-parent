package cn.wanru.rpc.registry.zookeeper;

import cn.wanru.rpc.registry.Instance;
import cn.wanru.rpc.registry.InstanceChangeListener;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author xxf
 * @since 2019/11/17
 */
class ListenerSupport {

    private CopyOnWriteArrayList<InstanceChangeListener> listeners = Lists.newCopyOnWriteArrayList();


    void addListener(InstanceChangeListener listener) {
        listeners.add(listener);
    }

    void removeListener(InstanceChangeListener listener) {
        listeners.remove(listener);
    }

    void fireInstanceChanged(String serviceName, List<Instance> services) {
        listeners.forEach(listener -> listener.onChanged(serviceName, services));
    }
}
