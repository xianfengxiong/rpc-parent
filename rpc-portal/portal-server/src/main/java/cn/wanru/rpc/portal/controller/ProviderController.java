package cn.wanru.rpc.portal.controller;

import cn.wanru.rpc.portal.model.ProviderDesc;
import cn.wanru.rpc.portal.model.param.ProviderStatusParam;
import cn.wanru.rpc.portal.model.param.ProviderWeightParam;
import cn.wanru.rpc.portal.service.ProviderService;
import cn.wanru.rpc.portal.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xxf
 * @since 2019/10/26
 */
@RestController
@RequestMapping("/api/provider")
public class ProviderController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProviderService providerService;

    /**
     * 获取服务下所有节点信息
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public String getProviders(
        @RequestParam(value = "env") int env,
        @RequestParam(value = "serviceName") String serviceName) {

        try {
            List<ProviderDesc> providerDescList = providerService.getProviders(env, serviceName);
            return JsonUtil.createSuccess(providerDescList);
        } catch (Exception e) {
            log.error("Get provider error,serviceName={}", serviceName, e);
            return JsonUtil.createFail(e.getMessage());
        }
    }

    @RequestMapping(value = "status", method = RequestMethod.PUT)
    public String updateStatus(@RequestBody ProviderStatusParam statusParam) {
        try {
            providerService.updateStatus(statusParam);
            return JsonUtil.createSuccess(null);
        } catch (Exception e) {
            log.error("Update status error,param={}", statusParam, e);
            return JsonUtil.createFail(e.getMessage());
        }
    }

    @RequestMapping(value = "weight", method = RequestMethod.PUT)
    public String updateWeight(@RequestBody ProviderWeightParam weightParam) {
        try {
            providerService.updateWeight(weightParam);
            return JsonUtil.createSuccess(null);
        } catch (Exception e) {
            log.error("Update weight error,param={}", weightParam, e);
            return JsonUtil.createFail(e.getMessage());
        }
    }
}
