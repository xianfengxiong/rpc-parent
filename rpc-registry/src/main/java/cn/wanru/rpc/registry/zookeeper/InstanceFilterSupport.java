package cn.wanru.rpc.registry.zookeeper;

import cn.wanru.rpc.registry.Instance;
import cn.wanru.rpc.registry.InstanceFilter;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @author xxf
 * @since 2019/11/17
 */
class InstanceFilterSupport {

    private CopyOnWriteArrayList<InstanceFilter> filters = Lists.newCopyOnWriteArrayList();

    void addInstanceFilter(InstanceFilter filter) {
        filters.add(filter);
    }

    void removeInstanceFilter(InstanceFilter filter) {
        filters.remove(filter);
    }

    List<Instance> filter(List<Instance> instances) {
        return instances.stream().filter(instance ->
            filters.stream().allMatch(filter -> filter.test(instance))
        ).collect(Collectors.toList());
    }

}
