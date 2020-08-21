package com.coronatracker.schedulers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import javax.jms.Queue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import com.coronatracker.models.CoronaStats;
import com.coronatracker.services.ICoronaTrackerService;
import com.coronatracker.utilities.JmsClient;

@Component
@EnableScheduling
public class StatsScheduler {

	@Autowired
	private ICoronaTrackerService trackerService;

	@Autowired
	private JmsClient jmsClient;

	@Autowired
	private Queue statsQueue;

	private Logger logger = LogManager.getLogger(getClass());

	@Scheduled(fixedDelay = 10 * 60 * 1000)
	private void processCoronaStats() {
		logger.info("Corona Stats cron start processing at : " + LocalDateTime.now());
		fetchCoronaStats();
		pushToQueue();
		logger.info("Corona Stats cron finished processing at : " + LocalDateTime.now());
	}

	/**
	 * private methods
	 */

	/**
	 * fetch corona stats from provider
	 */
	private void fetchCoronaStats() {
		try {
			trackerService.fetchCoronaStats();
		} catch (IOException e) {
			logger.error("Exception while fetching corona stats at: {} exception: {}", LocalDateTime.now(),
					e.getMessage());
		}
	}

	/**
	 * push stats to stream
	 */
	private void pushToQueue() {
		List<CoronaStats> coronaStats = trackerService.getAllCoronaStats();
		if (!CollectionUtils.isEmpty(coronaStats)) {
			jmsClient.pushMessage(statsQueue, coronaStats);
			logger.debug("stats pushed to queue, message: {}", coronaStats);
			return;
		}
		logger.debug("empty stats, not pushing to queue");
	}
}
