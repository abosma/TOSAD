package tosad.com.model.enums;

public enum ValueType {
	STATIC_NUMBER("STATIC_NUMBER"), STATIC_STRING("STATIC_STRING"), TUPLE("TUPLE"), ENTITY("ENTITY");

	private final String text;

	private ValueType(final String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}
}