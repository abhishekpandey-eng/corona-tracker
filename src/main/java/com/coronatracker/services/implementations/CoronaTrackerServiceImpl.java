package com.coronatracker.services.implementations;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
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

	@Value("${spring.ct.covid19Country}")
	private String covid19Country;

	@Autowired
	private HttpClient httpClient;

	private List<CoronaStats> allStats;

	private Logger logger = LogManager.getLogger(getClass());

	/**
	 * fetch stats of covid19 from given provider (Github) and transforming as per our need.
	 */
	@Override
	public void fetchCoronaStats() throws IOException {
		Optional<String> trackerResponse = httpClient.executeRequest(providerUrl, String.class, HttpMethod.GET, null);
		if (!trackerResponse.isPresent()) {
			logger.debug("empty response received after hitting provider url at: {}", LocalDateTime.now());
			return;
		}

		StringReader trackerResponseCsv = new StringReader(trackerResponse.get());
		Iterable<CSVRecord> stats = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(trackerResponseCsv);
		List<CoronaStats> newStats = StreamSupport.stream(stats.spliterator(), false)
				.filter(csvRecord -> csvRecord.get("Country/Region").equals(covid19Country))
				.map(csvRecord -> getCoronaStats(csvRecord)).collect(Collectors.toList());

		if (CollectionUtils.isEmpty(newStats)) {
			logger.debug("empty stats after filtering for required country at: {}", LocalDateTime.now());
			return;
		}

		/**
		 * Note: we can set this response in cache like Redis (in case of distributed
		 * env) and will use lock techniques like "Shed lock" so that scheduler of one
		 * Application instance will run at a time.
		 */
		this.allStats = newStats;
		logger.info("new tracker response set at: {}", LocalDateTime.now());
	}

	@Override
	public List<CoronaStats> getAllCoronaStats() {
		return allStats;
	}

	@Override
	public String getCovid19Country() {
		return covid19Country;
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
