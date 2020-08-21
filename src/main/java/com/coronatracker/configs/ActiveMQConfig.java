package com.coronatracker.configs;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import javax.jms.Queue;

@Configuration
@EnableJms
public class ActiveMQConfig {
	
	public static final String STATS_QUEUE = "stats.queue";

	@Bean
	public Queue statsJMSQueue() {
		return new ActiveMQQueue(STATS_QUEUE);
	}
}