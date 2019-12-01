package cn.wanru.rpc.util;

import com.google.common.base.Strings;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author xxf
 * @since 2019/10/27
 */
public abstract class NetUtils {

    private static AtomicReference<String> localIpRef = new AtomicReference<>("");

    public static String localIp() {
        if (Strings.isNullOrEmpty(localIpRef.get())) {
            localIpRef.compareAndSet("", findFirstNonLoopbackAddress().getHostAddress());
        }
        return localIpRef.get();
    }

    public static InetAddress findFirstNonLoopbackAddress() {
        InetAddress result = null;
        try {
            int lowest = Integer.MAX_VALUE;
            for (Enumeration<NetworkInterface> nics = NetworkInterface
                .getNetworkInterfaces(); nics.hasMoreElements(); ) {
                NetworkInterface ifc = nics.nextElement();
                if (ifc.isUp()) {
                    if (ifc.getIndex() < lowest || result == null) {
                        lowest = ifc.getIndex();
                    } else if (result != null) {
                        continue;
                    }
                    for (Enumeration<InetAddress> addrs = ifc
                        .getInetAddresses(); addrs.hasMoreElements(); ) {
                        InetAddress address = addrs.nextElement();
                        if (address instanceof Inet4Address
                            && !address.isLoopbackAddress()) {
                            result = address;
                        }
                    }
                }
            }
        } catch (IOException ex) {
            ExceptionUtils.rethrowRuntimeException(ex);
        }

        if (result != null) {
            return result;
        }

        try {
            return InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            ExceptionUtils.rethrowRuntimeException(e);
        }

        return null;
    }

}
