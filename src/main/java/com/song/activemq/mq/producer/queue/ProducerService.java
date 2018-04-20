package com.song.activemq.mq.producer.queue;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.song.base.ProducerFacadeService;

@Service
@Transactional
public class ProducerService implements ProducerFacadeService  {

	/**
	 * 通过@Qualifier修饰符来注入对应的bean
	 */
	@Autowired  
	@Qualifier("jmsQueueTemplate")
    private JmsTemplate jmsTemplate;
	
	/**
	 * 向指定队列发送消息
	 */
	@Override
	public void sendMessage(String queueName, String message) {
		jmsTemplate.send(queueName, new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(message);
			}
		});
	}
	
}
