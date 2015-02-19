package org.xllapp.log.logback;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 此类提供获取主机信息的工具方法.
 * 
 * @author dylan.chen Dec 27, 2013
 * 
 */
public abstract class HostInfoHelper {

	private final static Logger logger = LoggerFactory.getLogger(HostInfoHelper.class);

	/**
	 * 获取主机名.
	 * 
	 * @param awareBase
	 * @return
	 */
	public static String getHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			logger.debug("failure to get hostname", e);
			return null;
		}
	}

	/**
	 * 获取IP地址.
	 * 
	 * @param awareBase
	 * @return
	 */
	public static List<String> getIpAddrs() {
		List<String> ipAddrs = new ArrayList<String>();
		try {
			for (Enumeration<?> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements();) {
				NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
				for (Enumeration<?> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements();) {
					InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
					if ((!inetAddr.isLoopbackAddress()) && (inetAddr instanceof Inet4Address)) {
						ipAddrs.add(inetAddr.getHostAddress());
					}
				}
			}
		} catch (Exception e) {
			logger.debug("failure to get ip", e);
		}
		return ipAddrs;
	}
	
}
