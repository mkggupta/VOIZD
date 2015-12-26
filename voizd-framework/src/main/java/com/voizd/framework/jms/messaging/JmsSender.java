package com.voizd.framework.jms.messaging;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import com.voizd.framework.jms.messaging.util.JMSUtils;
import com.voizd.framework.jms.messaging.util.RoutingInfo;
import com.voizd.framework.jms.messaging.util.RoutingSlipAccessor;

/**
 * This is a utility class that abstracts the message sending details.
 * Implementations that used the header property directly have been moved to
 * this class.Users are encouraged to use the sendMessage method which sends an
 * ObjectMessage. The use of header properties for sending data to agents is
 * faster as compared to Object message but the difference is negligible for
 * objects with small payload. Hence for text only messages the Object Message
 * is a nice compromise between generality and performance. The following code
 * snippet shows a typical use of this class.
 * 
 * <br/>
 * <code>
 * <br/>RoutingInfo info = new RoutingInfo();
 * <br/>info.setAgentHeader(a:friendmgr);
 * <br/>info.setMimeType(JMSUtils.MIME_TYPE_TEXT);
 * <br/>info.setUserid(123);
 * <br/>info.setVendor('TOMO');
 * <br/>info.setRequestid((String)getActionContext().getObject("requestId"));
 * <br/>jmsSender.sendMesage(message, info);
 * </code>
 * 
 * @author 
 * 
 */
public class JmsSender {
	// logger
	private static Logger logger = LoggerFactory.getLogger(JmsSender.class);

	private JmsTemplate jmsTemplate;
	private RoutingSlipAccessor routingAccessor;

	public void setRoutingAccessor(RoutingSlipAccessor routingAccessor) {
		this.routingAccessor = routingAccessor;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	public void sendMesage(final Serializable message, RoutingInfo info)
			throws Exception {
		jmsTemplate.send(new MessageSender(message, info));
		

	}

	

	class MessageSender implements MessageCreator {
		private Serializable message;
		private RoutingInfo info;

		public MessageSender(Serializable message, RoutingInfo info) {
			this.message = message;
			this.info = info;

		}

		public Message createMessage(Session session) throws JMSException {
			ObjectMessage jmsMessage = session.createObjectMessage();

			try {
				jmsMessage.setObject(message);
				jmsMessage = (ObjectMessage) routingAccessor
						.populateRoutingInfo(jmsMessage, info);
				logger.info("routing Slip="
						+ jmsMessage.getStringProperty(JMSUtils.ROUTING_SLIP));

			} catch (Exception e) {
				logger.error("failed pushing statistics object onto queue", e);
			}
			return jmsMessage;
		}
	}

}
