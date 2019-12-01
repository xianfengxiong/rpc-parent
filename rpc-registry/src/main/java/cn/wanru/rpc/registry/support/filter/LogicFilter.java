package cn.wanru.rpc.registry.support.filter;

import cn.wanru.rpc.registry.Instance;
import cn.wanru.rpc.registry.InstanceFilter;
import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * @author xxf
 * @since 2019/12/1
 */
public class LogicFilter {

    public InstanceFilter and(InstanceFilter... filters) {
        ImmutableList<InstanceFilter> list =
            ImmutableList.<InstanceFilter>builder().add(filters).build();
        return new AndFilter(list);
    }

    public InstanceFilter or(InstanceFilter... filters) {
        ImmutableList<InstanceFilter> list =
            ImmutableList.<InstanceFilter>builder().add(filters).build();
        return new OrFilter(list);
    }

    public InstanceFilter not(InstanceFilter filter) {
        return new NotFilter(filter);
    }


    static class AndFilter implements InstanceFilter {

        private List<InstanceFilter> filters;

        AndFilter(List<InstanceFilter> filters) {
            this.filters = filters;
        }

        @Override
        public boolean test(Instance instance) {
            for (InstanceFilter filter : filters) {
                if (!filter.test(instance)) {
                    return false;
                }
            }
            return true;
        }
    }

    static class OrFilter implements InstanceFilter {

        private List<InstanceFilter> filters;

        OrFilter(List<InstanceFilter> filters) {
            this.filters = filters;
        }

        @Override
        public boolean test(Instance instance) {
            for (InstanceFilter filter : filters) {
                if (filter.test(instance)) {
                    return true;
                }
            }
            return false;
        }
    }

    static class NotFilter implements InstanceFilter {

        private InstanceFilter filter;

        NotFilter(InstanceFilter filter) {
            this.filter = filter;
        }

        @Override
        public boolean test(Instance instance) {
            return !filter.test(instance);
        }
    }

}
