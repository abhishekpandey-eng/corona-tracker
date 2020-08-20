package com.coronatracker.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CoronaStats {
	
	private String state;
	
	private String country;
	
	private int totalCases;
	
	private int prevDayCases;
}
