package org.xllapp.log.logback;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * 错误消息.
 * 
 * @author dylan.chen Dec 27, 2013
 * 
 */
public class ErrorMessage implements Serializable {

	private static final long serialVersionUID = 7569348369237044348L;

	private String hostName;

	private List<String> ipAddrs;

	private String moduleName;

	private String loggerName;

	private String throwableClass;

	private String errorMsg;

	private String throwableStackTrace;

	private String timeStamp;

	public String getHostName() {
		return this.hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public List<String> getIpAddrs() {
		return this.ipAddrs;
	}

	public void setIpAddrs(List<String> ipAddrs) {
		this.ipAddrs = ipAddrs;
	}

	public String getModuleName() {
		return this.moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getLoggerName() {
		return this.loggerName;
	}

	public void setLoggerName(String loggerName) {
		this.loggerName = loggerName;
	}

	public String getThrowableClass() {
		return this.throwableClass;
	}

	public void setThrowableClass(String throwableClass) {
		this.throwableClass = throwableClass;
	}

	public String getErrorMsg() {
		return this.errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getThrowableStackTrace() {
		return this.throwableStackTrace;
	}

	public void setThrowableStackTrace(String throwableStackTrace) {
		this.throwableStackTrace = throwableStackTrace;
	}

	public String getTimeStamp() {
		return this.timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = DateFormatUtils.format(timeStamp, "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}

}