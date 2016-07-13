package at.fhj.swd.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.fhj.swd.service.CurrencyInfo;

public class CSVCurrencyService implements CurrencyService {

	private String endpoint;

	@Override
	public String getApiUrl() {
		return endpoint;
	}

	@Override
	public void setApiUrl(String endpoint) {
		this.endpoint = endpoint;
	}

	@Override
	public List<CurrencyInfo> getCurrencies() throws CurrencyServiceException {
		List<CurrencyInfo> list = new ArrayList<CurrencyInfo>();
		String line = "";

		// https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new URL(endpoint).openStream()))) {
			while ((line = br.readLine()) != null) {
				String[] split = line.split(";");
				list.add(new CurrencyInfo(split[0], split[1]));
			}
		} catch (Exception e) {
			throw new CurrencyServiceException("Error retrieving currency info", e);
		}

		return list;
	}

	@Override
	public Map<String, Float> getRatesForCurrency(String rate) throws CurrencyServiceException {
		Map<String, Float> map = new HashMap<String, Float>();
		String line = "";

		// https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new URL(endpoint).openStream()))) {
			while ((line = br.readLine()) != null) {
				String[] split = line.split(";");
				map.put(split[0], Float.parseFloat(split[2]));
			}
		} catch (Exception e) {
			throw new CurrencyServiceException("Error retrieving exchange rates", e);
		}

		return map;
	}

}
