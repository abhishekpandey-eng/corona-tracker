package com.coronatracker.configs;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import javax.jms.Queue;

@Configuration
@EnableJms
public class ActiveMQConfig {
	
	@Value("${spring.ct.queue.name}")
	private String statsQueue;

	@Bean
	public Queue statsJMSQueue() {
		return new ActiveMQQueue(statsQueue);
	}
}