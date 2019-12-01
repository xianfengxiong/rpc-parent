package cn.wanru.rpc.portal.controller;


import cn.wanru.rpc.portal.service.AppService;
import cn.wanru.rpc.portal.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xxf
 * @since 2019/10/26
 */
@RestController
@RequestMapping("/api/app")
public class AppController {

    @Autowired
    private AppService appService;

    @RequestMapping(method = RequestMethod.GET)
    public String getAppList() {
        List<String> appList = appService.getAppList();
        if (appList != null) {
            return JsonUtil.createSuccess(appList);
        } else {
            return JsonUtil.createFail("服务获取失败");
        }
    }

}
