package com.coronatracker.services;

import java.io.IOException;
import java.util.List;

import com.coronatracker.models.CoronaStats;

public interface ICoronaTrackerService {

	void fetchCoronaStats() throws IOException;
	
	List<CoronaStats> getAllCoronaStats();
}
