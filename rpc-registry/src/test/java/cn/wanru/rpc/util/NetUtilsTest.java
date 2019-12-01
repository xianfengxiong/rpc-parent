package cn.wanru.rpc.util;

import org.junit.Test;

import java.net.InetAddress;

/**
 * @author xxf
 * @since 2019/10/28
 */
public class NetUtilsTest {

    @Test
    public void testGetIp() {
        System.out.println(NetUtils.localIp());
    }

}
