package cn.wanru.rpc.portal.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author xxf
 * @since 2019/10/26
 */
@ConfigurationProperties(prefix = "zk")
public class ZookeeperProperties {

    private String basePath = "/services";

    private String connectionString = "localhost:2181";

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }
}
