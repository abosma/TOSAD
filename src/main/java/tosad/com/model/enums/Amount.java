package tosad.com.model.enums;

public enum Amount {
	SINGLE("SINGLE"), DOUBLE("DOUBLE"), MULTIPLE("MULTIPLE");

	private final String text;

	private Amount(final String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}
}