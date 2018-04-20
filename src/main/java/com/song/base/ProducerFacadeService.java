package com.song.base;

public interface ProducerFacadeService {

	/**
	 * 向指定队列发送消息
	 * @param queueName
	 * @param message
	 */
	public void sendMessage(String queueName, String message);
	
}
