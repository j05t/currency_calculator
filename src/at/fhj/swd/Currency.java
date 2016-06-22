package at.fhj.swd;

public class Currency {
	private String shortName;
	private String fullName;
	
	public Currency(String shortName, String fullName) {
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
