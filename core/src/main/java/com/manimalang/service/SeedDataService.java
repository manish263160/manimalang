package com.manimalang.service;

import java.util.List;
import java.util.Map;

import com.manimalang.models.CitySeed;
import com.manimalang.models.Country;


public interface SeedDataService {
	
	List<Country> getAllCountries();
	
		
	List<CitySeed> getAllCitiesFromCountryId(long countryId);

	Map<Integer, String> getAllCurrencyUnit();
	Map<Integer, String> getAllCurrencyUnitCode();
	
}
