package cn.wanru.rpc.portal.service;

import cn.wanru.rpc.portal.service.curator.ZookeeperClient;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class AppService {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ZookeeperClient zkClient;

    public List<String> getAppList() {
        try {
            return zkClient.getAllService();
        } catch (Exception e) {
            log.error("get appList failed: {}", e);
            return Collections.emptyList();
        }
    }
}
