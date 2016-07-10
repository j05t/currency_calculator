package at.fhj.swd.ui;

public class CurrencyInfo {
	private String shortName;
	private String fullName;
	
	public CurrencyInfo(String shortName, String fullName) {
		setShortName(shortName);
		setFullName(fullName);
	}
	
	public String toString() {
		return fullName + " (" + shortName + ")";
	}
	
	
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

}
