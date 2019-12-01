package cn.wanru.rpc.registry;

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.Map;

import static cn.wanru.rpc.registry.Constants.GROUP_DEFAULT;
import static cn.wanru.rpc.registry.Constants.STATUS_UP;
import static cn.wanru.rpc.util.NetUtils.localIp;

/**
 * 代表服务的一个实例
 *
 * @author xxf
 * @since 2019/10/27
 */
public class Instance {

    // ip : port
    private String id;

    private String serviceName;

    private String ip;

    private int port;

    private String group;

    private String status;

    private long lastUpdateTime;

    private Map<String, String> metadata = new HashMap<>();

    // region Getter

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }


    // endregion


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Instance{");
        sb.append("id='").append(id).append('\'');
        sb.append(", serviceName='").append(serviceName).append('\'');
        sb.append(", ip='").append(ip).append('\'');
        sb.append(", port=").append(port);
        sb.append(", group='").append(group).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append(", lastUpdateTime=").append(lastUpdateTime);
        sb.append(", metadata=").append(metadata);
        sb.append('}');
        return sb.toString();
    }

    public static InstanceBuilder newBuidler() {
        return new InstanceBuilder();
    }

    public static class InstanceBuilder {

        private String serviceName;

        private String ip;

        private int port;

        private String group = GROUP_DEFAULT;

        private String status = STATUS_UP;

        InstanceBuilder() {
            this.ip = localIp();
        }

        public InstanceBuilder serviceName(String serviceName) {
            this.serviceName = serviceName;
            return this;
        }

        public InstanceBuilder port(int port) {
            this.port = port;
            return this;
        }

        public InstanceBuilder group(String group) {
            this.group = group;
            return this;
        }

        public InstanceBuilder status(String status) {
            this.status = status;
            return this;
        }

        public Instance build() {
            Preconditions.checkNotNull(ip);
            Preconditions.checkState(port > 0);

            Instance instance = new Instance();
            instance.id = ip + ":" + port;
            instance.ip = ip;
            instance.port = port;
            instance.serviceName = serviceName;
            instance.status = status;
            instance.group = group;
            instance.lastUpdateTime = System.currentTimeMillis();

            return instance;
        }
    }

}
