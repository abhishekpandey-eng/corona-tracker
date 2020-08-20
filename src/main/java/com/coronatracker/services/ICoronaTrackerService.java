package com.coronatracker.services;

import java.util.List;
import com.coronatracker.models.CoronaStats;

public interface ICoronaTrackerService {

	void fetchCoronaStats();
	
	List<CoronaStats> getAllCoronaStats();
}
