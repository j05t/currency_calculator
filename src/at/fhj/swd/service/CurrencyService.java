package at.fhj.swd.service;

import java.util.List;
import java.util.Map;

import at.fhj.swd.ui.CurrencyInfo;

public interface CurrencyService {
	String getApiUrl();
	void setApiUrl(String endpoint);
	
	List<CurrencyInfo> getCurrencies() throws CurrencyServiceException;
	Map<String, Float> getRatesForCurrency(String rate);
}
