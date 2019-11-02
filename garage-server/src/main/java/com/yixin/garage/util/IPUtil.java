package com.yixin.garage.util;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baomidou.mybatisplus.core.toolkit.StringPool;


public class IPUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(IPUtil.class);

    private static final String ipPattern = "^(?:(?:[01]?\\d{1,2}|2[0-4]\\d|25[0-5])\\.){3}(?:[01]?\\d{1,2}|2[0-4]\\d|25[0-5])\\b";

    /**
     * @Description: 获取请求中的ip地址：过了多级反向代理，获取的ip不是唯一的，二是包含中间代理层ip。
     * 
     * @return 可能有多个，例如：192.168.1.110， 192.168.1.120
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = "127.0.0.1";
        if (request != null) {
            ip = request.getHeader("x-forwarded-for");
            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }

            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }

            if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        }
        return ip;

    }

    /**
     * 
     * @Description: 获取客户端请求中的真实的ip地址
     * 
     * 获取客户端的IP地址的方法是：request.getRemoteAddr()，这种方法在大部分情况下都是有效的。
     * 但是在通过了Apache，Squid等反向代理软件就不能获取到客户端的真实IP地址。而且，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，
     * 而是一串ip值，例如：192.168.1.110， 192.168.1.120， 192.168.1.130， 192.168.1.100。其中第一个192.168.1.110才是用户真实的ip
     */
    public static String getRealIp(HttpServletRequest request) {
        String ip = "127.0.0.1";
        if (request == null) {
            return ip;
        }

        ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")) {
                //根据网卡取本机配置的IP  
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                    ip = inet.getHostAddress();
                } catch (UnknownHostException e) {
                    LOGGER.error("getRealIp occurs error, caused by: ", e);
                }
            }
        }

        if (ip != null && ip.length() > 15) { //"***.***.***.***".length() = 15  
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        return ip;
    }

    /**
     * 获取服务器IP
     */
    public static String getServiceIp() {
        Enumeration<NetworkInterface> netInterfaces = null;
        String ipsStr = "";

        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> ips = ni.getInetAddresses();
                Pattern pattern = Pattern.compile(ipPattern);
                while (ips.hasMoreElements()) {
                    String ip = ips.nextElement().getHostAddress();
                    Matcher matcher = pattern.matcher(ip);
                    if (matcher.matches() && !ip.equals("127.0.0.1")) {
                        ipsStr = ip;
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("getServiceIp occurs error, caused by: ", e);
        }

        return ipsStr;
    }
    /**
     * 检查IP是否合法
     * @param ip
     * @return
     */
    public static boolean ipValid(String ip) {
        String regex0 = "(2[0-4]\\d)" + "|(25[0-5])";
        String regex1 = "1\\d{2}";
        String regex2 = "[1-9]\\d";
        String regex3 = "\\d";
        String regex = "(" + regex0 + ")|(" + regex1 + ")|(" + regex2 + ")|(" + regex3 + ")";
        regex = "(" + regex + ").(" + regex + ").(" + regex + ").(" + regex  + ")";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(ip);
        return m.matches();
    }

    /**
     * 获取本地ip 适合windows与linux
     *
     * @return
     */
    public static String getLocalIP() {
        String localIP = "127.0.0.1";
        try {
            Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                InetAddress ip = ni.getInetAddresses().nextElement();
                if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
                    localIP = ip.getHostAddress();
                    break;
                }
            }
        } catch (Exception e) {
            try {
                localIP = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            }
        }
        return localIP;
    }

    /**
     * 获取客户机的ip地址
     * @param request
     * @return
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("http_client_ip");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        // 如果是多级代理，那么取第一个ip为客户ip
        if (ip != null && ip.indexOf(",") != -1) {
            ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
        }
        return ip;
    }


    /**
     * 把ip转化为整数
     * @param ip
     * @return
     */
    public static long translateIP2Int(String ip){
        String[] intArr = ip.split("\\.");
        int[] ipInt = new int[intArr.length];
        for (int i = 0; i <intArr.length ; i++) {
            ipInt[i] = new Integer(intArr[i]).intValue();
        }
        return ipInt[0] * 256 * 256 * 256 + + ipInt[1] * 256 * 256 + ipInt[2] * 256 + ipInt[3];
    }
    private final  static long datacenterIdBits = 5L;
    private final static long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    
    protected static long getDatacenterId(long maxDatacenterId) {
        long id = 0L;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                id = 1L;
            } else {
                byte[] mac = network.getHardwareAddress();
                if (null != mac) {
                    id = ((0x000000FF & (long) mac[mac.length - 1]) | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
                    id = id % (maxDatacenterId + 1);
                }
            }
        } catch (Exception e) {
            LOGGER.warn(" getDatacenterId: " + e.getMessage());
        }
        return id;
    }
    
    protected static long getMaxWorkerId(long datacenterId, long maxWorkerId) {
        StringBuilder mpid = new StringBuilder();
        mpid.append(datacenterId);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (StringUtils.isNotEmpty(name)) {
            /*
             * GET jvmPid
             */
            mpid.append(name.split(StringPool.AT)[0]);
        }
        /*
         * MAC + PID 的 hashcode 获取16个低位
         */
        return (mpid.toString().hashCode() & 0xffff) % (maxWorkerId + 1);
    }
    
    public static void main(String[] args) {
//        System.out.println(getLocalIP());
//        System.out.println(getServiceIp());
//        System.out.println(-1L << datacenterIdBits);
//        System.out.println("maxDatacenterId:"+maxDatacenterId);
//        long getDatacenterId = getDatacenterId(maxDatacenterId);
//        LOGGER.info("getDatacenterId:{}",getDatacenterId);
//        
//        long  getMaxWorkerId = getMaxWorkerId(getDatacenterId,maxDatacenterId);
//        System.out.println(getMaxWorkerId);
        
        
        int n = 31;
        Double d = Math.sqrt(n);
        System.out.println(d);
        
        System.out.println(Math.log(n)/Math.log(5));
        int loop = (int) (Math.log(n)/Math.log(5));
        int  sum = 0;
        for(int i=1; i<=loop; i++){
        	sum += n/Math.pow(5, i);
        }
        System.out.println(sum);
    }
}

