package cn.wanru.rpc.registry.zookeeper;

import cn.wanru.rpc.registry.RegistryCenterConfig;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * @author xxf
 * @since 2019/11/17
 */
public class ZookeeperRegistryCenterConfig extends RegistryCenterConfig {

    private String basePath = "/services";

    private String connectionString = "localhost:2181";


    public String getConnectionString() {
        return this.connectionString;
    }

    public void setConnectionString(String connectionString) {
        Preconditions.checkNotNull(connectionString, "Connection string can not be null");
        this.connectionString = connectionString;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        if (Strings.isNullOrEmpty(basePath) || basePath.indexOf(0) != '/') {
            throw new IllegalArgumentException("Base path must be start with '/' ");
        }
        this.basePath = basePath;
    }
}
