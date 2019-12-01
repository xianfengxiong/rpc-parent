package cn.wanru.rpc.portal.model.param;

/**
 * @author xxf
 * @since 2019/12/1
 */
public class ProviderWeightParam {
    private int env;
    private String serviceName;
    private String ip;
    private int port;
    private float weight;

    // region Getter/Setter

    public int getEnv() {
        return env;
    }

    public void setEnv(int env) {
        this.env = env;
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

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }


    // endregion

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ProviderWeightParam{");
        sb.append("env=").append(env);
        sb.append(", serviceName='").append(serviceName).append('\'');
        sb.append(", ip='").append(ip).append('\'');
        sb.append(", port=").append(port);
        sb.append(", weight=").append(weight);
        sb.append('}');
        return sb.toString();
    }
}
