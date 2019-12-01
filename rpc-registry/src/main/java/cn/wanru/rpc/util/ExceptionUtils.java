package cn.wanru.rpc.util;

/**
 * @author xxf
 * @since 2019/10/27
 */
public final class ExceptionUtils {

    private ExceptionUtils() {
    }

    public static void rethrowRuntimeException(Exception e) {
        throw new RuntimeException(e);
    }

}
