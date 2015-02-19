package org.xllapp.log.logback;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import ch.qos.logback.classic.filter.ThresholdFilter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.filter.Filter;


/**
 * 基于JMS的Appender实现，用于处理大等于ERROR等级的日志.
 * 
 * @author dylan.chen Dec 27, 2013
 * 
 */
public class ErrorJMSAppender<E> extends UnsynchronizedAppenderBase<E> {

	private String moduleName;
	
	private String brokerUrl;

	private String userName;

	private String password;
	
	private JMSProducer jmsProducer;

	{
		ThresholdFilter thresholdFilter = new ThresholdFilter();
		thresholdFilter.setLevel("ERROR");
		thresholdFilter.start();
		this.addFilter((Filter) thresholdFilter);

		final UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {

			@Override
			public void uncaughtException(Thread t, Throwable e) {

				addInfo("handling uncaught exception", e);

				sendMessage(getUncaughtErrorMessage(e));

				if (defaultUncaughtExceptionHandler != null) {
					defaultUncaughtExceptionHandler.uncaughtException(t, e);
				}

			}

		});
		
	}

	@Override
	protected void append(E eventObject) {

		if (!(eventObject instanceof ILoggingEvent)) {
			addInfo("unsupported event[" + eventObject.getClass() + "]");
			return;
		}

		sendMessage(getErrorMessage((ILoggingEvent) eventObject));

	}

	private ErrorMessage getUncaughtErrorMessage(Throwable throwable) {
		
		ErrorMessage message = new ErrorMessage();
		message.setHostName(HostInfoHelper.getHostName());
		message.setIpAddrs(HostInfoHelper.getIpAddrs());
		message.setModuleName(this.moduleName);

		message.setLoggerName(this.getClass().getPackage().getName()+".UncaughtExceptionHandler");
		message.setErrorMsg(throwable.getLocalizedMessage());
		message.setThrowableClass(throwable.getClass().getName());
		message.setThrowableStackTrace(ExceptionUtils.getStackTrace(throwable));
		message.setTimeStamp(new Date());
		return message;
	}

	private ErrorMessage getErrorMessage(ILoggingEvent event) {
		
		ErrorMessage message = new ErrorMessage();
		message.setHostName(HostInfoHelper.getHostName());
		message.setIpAddrs(HostInfoHelper.getIpAddrs());
		message.setModuleName(this.moduleName);

		message.setLoggerName(event.getLoggerName());
		message.setErrorMsg(event.getFormattedMessage());
		IThrowableProxy throwableProxy = event.getThrowableProxy();
		if (null != throwableProxy) {
			if (StringUtils.isBlank(message.getErrorMsg())) {
				message.setErrorMsg(throwableProxy.getMessage());
			}
			message.setThrowableClass(throwableProxy.getClassName());
			message.setThrowableStackTrace(ThrowableProxyUtil.asString(throwableProxy));
		}

		message.setTimeStamp(new Date(event.getTimeStamp()));
		return message;
	}

	private void sendMessage(ErrorMessage message) {
		try {
			
			synchronized (this) {
				if(null==this.jmsProducer){
					this.jmsProducer=new JMSProducer(this.brokerUrl, this.userName, this.password);
				}
			}
			
			this.jmsProducer.send(message);
			
		} catch (Exception e) {
			addError("failure to send message[" + message + "]", e);
		}
	}

	public String getModuleName() {
		return this.moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getBrokerUrl() {
		return brokerUrl;
	}

	public void setBrokerUrl(String brokerUrl) {
		this.brokerUrl = brokerUrl;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
