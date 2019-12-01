package cn.wanru.rpc.portal.service;

import cn.wanru.rpc.portal.model.ProviderDesc;
import cn.wanru.rpc.portal.model.StatusConverter;
import cn.wanru.rpc.portal.model.param.ProviderStatusParam;
import cn.wanru.rpc.portal.model.param.ProviderWeightParam;
import cn.wanru.rpc.portal.service.curator.ZookeeperClient;
import cn.wanru.rpc.registry.Constants;
import cn.wanru.rpc.registry.Instance;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

import static cn.wanru.rpc.registry.Constants.STATUS_UP;

/**
 * @author xxf
 * @since 2019/10/26
 */
@Service
public class ProviderService {

    @Autowired
    private ZookeeperClient zookeeperClient;

    public List<ProviderDesc> getProviders(int env, String serviceName) throws Exception {
        List<Instance> instances = zookeeperClient.getInstances(serviceName);
        return transToVos(instances, env);
    }

    private List<ProviderDesc> transToVos(List<Instance> services, int env) {
        if (CollectionUtils.isEmpty(services)) {
            return Collections.emptyList();
        }
        List<ProviderDesc> result = Lists.newArrayListWithExpectedSize(services.size());
        for (Instance service : services) {
            result.add(transToVo(service, env));
        }
        return result;
    }

    private ProviderDesc transToVo(Instance service, int env) {
        ProviderDesc desc = new ProviderDesc();
        desc.setAppkey(service.getServiceName());
        desc.setIp(service.getIp());
        desc.setPort(service.getPort());
        desc.setLastUpdateTime(service.getLastUpdateTime() / 1000);
        desc.setEnv(env);
        desc.setStatus(StatusConverter.stringToIntStatus(service.getStatus()));
        return desc;
    }

    public void updateStatus(ProviderStatusParam statusParam) throws Exception {
        Instance instance = zookeeperClient.getInstance(
            statusParam.getServiceName(), statusParam.getIp(), statusParam.getPort());
        instance.setStatus(StatusConverter.intToStringStatus(statusParam.getStatus()));
        instance.setLastUpdateTime(System.currentTimeMillis());
        zookeeperClient.updateInstance(instance);
    }

    public void updateWeight(ProviderWeightParam weightParam) throws Exception {
        Instance instances = zookeeperClient.getInstance(
            weightParam.getServiceName(), weightParam.getIp(), weightParam.getPort());
        instances.setWeight(weightParam.getWeight());
        instances.setLastUpdateTime(System.currentTimeMillis());
        zookeeperClient.updateInstance(instances);
    }
}
