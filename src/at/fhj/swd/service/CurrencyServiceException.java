package at.fhj.swd.service;

public class CurrencyServiceException extends Exception {

	public CurrencyServiceException(String message) {
		super(message);
	}

	public CurrencyServiceException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
