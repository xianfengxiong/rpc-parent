package cn.wanru.rpc.portal;

import cn.wanru.rpc.portal.config.ZookeeperProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author xxf
 * @since 2019/10/27
 */
@SpringBootApplication
@EnableConfigurationProperties(ZookeeperProperties.class)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
