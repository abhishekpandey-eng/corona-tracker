package com.coronatracker.utilities;

import javax.jms.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class JmsClient {

	@Autowired
	private JmsMessagingTemplate jmsTemplate;

	public void pushMessage(Queue queue, Object payload) {
		jmsTemplate.convertAndSend(queue, payload);
	}
}
