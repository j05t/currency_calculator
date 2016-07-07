package at.fhj.swd.service;

import java.util.List;
import java.util.Map;

public interface CurrencyService {
	String getApiUrl();
	void setApiUrl(String endpoint);
	
	List<CurrencyInfo> getCurrencies();
	Map<String, Float> getRatesForCurrency(String rate);
}
