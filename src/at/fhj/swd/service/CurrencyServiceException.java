package at.fhj.swd.service;

public class CurrencyServiceException extends RuntimeException {

	public CurrencyServiceException(String message) {
		super(message);
	}

	public CurrencyServiceException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
