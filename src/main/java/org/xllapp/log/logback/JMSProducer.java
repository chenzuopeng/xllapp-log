package org.xllapp.log.logback;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 此类用于发送JMS消息.
 * 
 * @author dylan.chen Aug 19, 2013
 * 
 */
public class JMSProducer {
	
	private final static Logger logger=LoggerFactory.getLogger(JMSProducer.class);

	private final static String QUEUE_NAME = "ICITY_ERROR_LOG";
	
	private ConnectionFactory connectionFactory;
	
	private ObjectMapper objectMapper=new ObjectMapper();
	
	public JMSProducer(String brokerUrl, String userName, String password) {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(userName, password, brokerUrl);
		activeMQConnectionFactory.setUseAsyncSend(true);
		this.connectionFactory = activeMQConnectionFactory;
	}

	public void send(ErrorMessage message) throws Exception {
		Connection connection = null;
		Session session = null;
		try {
			connection = this.connectionFactory.createConnection();
			connection.start();
			session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue(QUEUE_NAME);
			MessageProducer producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.PERSISTENT);
			String json=this.objectMapper.writeValueAsString(message);
			producer.send(session.createTextMessage(json));
			
			logger.debug("sended message[{}]",message);
			
		}catch (Exception e) {
			
			logger.error("failure to send message[" + message + "]", e);
			
		} finally {
			closeSession(session);
			closeConnection(connection);
		}
	}

	private void closeSession(Session session) {
		try {
			if (session != null) {
				session.close();
			}
		} catch (Exception e) {
		}
	}

	private void closeConnection(Connection connection) {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (Exception e) {
		}
	}

}
