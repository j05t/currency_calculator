package at.fhj.swd.service;

public class CurrencyInfo {
	private String name;
	private String displayName;

	public CurrencyInfo(String name, String displayName) {
		setName(name);
		setDisplayName(displayName);
	}

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	private void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public String toString() {
		return String.format("%s (%s)", getDisplayName(), getName());
	}
}
