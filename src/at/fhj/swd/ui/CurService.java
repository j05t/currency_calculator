package at.fhj.swd.ui;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import at.fhj.swd.service.CurrencyInfo;
import at.fhj.swd.service.CurrencyService;

public class CurService implements CurrencyService {

	private String endpoint;
	
	@Override
	public String getApiUrl() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setApiUrl(String endpoint) {
		this.endpoint = endpoint;
	}

	@Override
	public List<CurrencyInfo> getCurrencies() {
		List<CurrencyInfo> list = new ArrayList<CurrencyInfo>();

		String line = "";
		// https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new URL(endpoint).openStream()))) {
			while ((line = br.readLine()) != null) {
				String[] split = line.split(";");
				list.add(new CurrencyInfo(split[0], split[1]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	@Override
	public Map<String, Float> getRatesForCurrency(String rate) {
		// TODO Auto-generated method stub
		return null;
	}

}
