package com.song.activemq.mq.consumer.queue;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.stereotype.Component;

/**
 * 消费者监听
 * @author songhj
 *
 */
/*@Component
public class QueueReceiver implements MessageListener {

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		try {
			TextMessage msg = (TextMessage)message;
			System.out.println("QueueReceiver接收到消息:" + msg.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}*/
