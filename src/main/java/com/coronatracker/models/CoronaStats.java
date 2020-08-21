package com.coronatracker.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CoronaStats implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1629722256084039270L;

	private String state;

	private String country;

	private int totalCases;

	private int prevDayCases;
}
