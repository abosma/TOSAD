package tosad.com.model.enums;

public enum ValueType {
	SINGLE("SINGLE"), DOUBLE("DOUBLE"), MULTIPLE("MULTI");

	private final String text;

	private ValueType(final String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}
}