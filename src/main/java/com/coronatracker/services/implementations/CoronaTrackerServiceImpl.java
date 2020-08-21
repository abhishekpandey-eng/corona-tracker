package com.coronatracker.services.implementations;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import com.coronatracker.models.CoronaStats;
import com.coronatracker.services.ICoronaTrackerService;
import com.coronatracker.utilities.HttpClient;

@Service
public class CoronaTrackerServiceImpl implements ICoronaTrackerService {

	@Value("${spring.ct.providerUrl}")
	private String providerUrl;

	@Autowired
	private HttpClient httpClient;

	private List<CoronaStats> allStats;

	private Logger logger = LogManager.getLogger(getClass());

	@Override
	@PostConstruct
	public void fetchCoronaStats() throws IOException {
		Optional<String> trackerResponse = httpClient.executeRequest(providerUrl, String.class, HttpMethod.GET, null);
		if (!trackerResponse.isPresent()) {
			logger.debug("empty response received after hitting provider url at: {}", LocalDateTime.now());
			return;
		}

		StringReader trackerResponseCsv = new StringReader(trackerResponse.get());
		Iterable<CSVRecord> stats = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(trackerResponseCsv);
		List<CoronaStats> newStats = StreamSupport.stream(stats.spliterator(), false)
				.filter(csvRecord -> csvRecord.get("Country/Region").equals("Canada"))
				.map(csvRecord -> getCoronaStats(csvRecord)).collect(Collectors.toList());

		if (CollectionUtils.isEmpty(newStats)) {
			logger.debug("empty stats after filtering for required country at: {}", LocalDateTime.now());
			return;
		}

		this.allStats = newStats;
		logger.info("new tracker response set at: {}", LocalDateTime.now());
	}

	@Override
	public List<CoronaStats> getAllCoronaStats() {
		return allStats;
	}

	/**
	 * private methods
	 */

	/**
	 * Used to map CSVRecord to Model obj
	 * 
	 * @param stat of CSVRecord type
	 * @return instance of CoronaStats
	 */
	private CoronaStats getCoronaStats(CSVRecord stat) {
		CoronaStats coronaStats = new CoronaStats();

		coronaStats.setState(stat.get("Province/State"));
		coronaStats.setCountry(stat.get("Country/Region"));
		int latestCases = Integer.parseInt(stat.get(stat.size() - 1));
		int prevDayCases = Integer.parseInt(stat.get(stat.size() - 2));
		coronaStats.setTotalCases(latestCases);
		coronaStats.setPrevDayCases(prevDayCases);

		return coronaStats;
	}
}
