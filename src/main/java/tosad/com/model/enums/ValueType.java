package tosad.com.model.enums;

public enum ValueType {
	LITERAL("LITERAL"), COLUMN("COLUMN"), TABLE("TABLE");

	private final String text;

	private ValueType(final String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}
}