package com.coronatracker.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.coronatracker.models.CoronaStats;
import com.coronatracker.services.ICoronaTrackerService;

@Controller
public class CoronaTrackerController {
	
	@Autowired
	private ICoronaTrackerService trackerService;
	
	/**
	 * returns home screen with Covid19 figure
	 * Note: we can give paginated response in future to support large dataset
	 * @param model
	 * @return
	 */
	@GetMapping(value = {"", "/"})
	public String home(Model model) {
		List<CoronaStats> coronaStats = trackerService.getAllCoronaStats();
		
		model.addAttribute("coronaStats", coronaStats);
		model.addAttribute("covid19Country", trackerService.getCovid19Country());

		return "home";
	}
}
