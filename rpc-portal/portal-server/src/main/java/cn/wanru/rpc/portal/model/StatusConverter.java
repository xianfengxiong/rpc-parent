package cn.wanru.rpc.portal.model;

import cn.wanru.rpc.registry.Constants;

/**
 * @author xxf
 * @since 2019/12/1
 */
public abstract class StatusConverter {

    public static final int status_dead = 0;
    public static final int status_starting = 1;
    public static final int status_alive = 2;
    public static final int status_stopping = 3;
    public static final int status_stopped = 4;

    /**
     * @param status
     * @return
     * @see Constants
     */
    public static int stringToIntStatus(String status) {
        if (Constants.STATUS_UP.equalsIgnoreCase(status)) {
            return status_alive;
        } else if (Constants.STATUS_DOWN.equalsIgnoreCase(status)) {
            return status_stopped;
        } else if (Constants.STATUS_OUT_OF_SERVICE.equalsIgnoreCase(status)) {
            return status_starting;
        } else {
            return status_dead;
        }
    }

    public static String intToStringStatus(int status) {
        switch (status) {
            case status_dead:
            case status_stopping:
            case status_stopped:
                return Constants.STATUS_DOWN;
            case status_alive:
                return Constants.STATUS_UP;
            case status_starting:
            default:
                return Constants.STATUS_OUT_OF_SERVICE;
        }
    }

}
