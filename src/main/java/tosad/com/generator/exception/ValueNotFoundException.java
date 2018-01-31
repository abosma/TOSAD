package tosad.com.generator.exception;

public class ValueNotFoundException extends Exception {
	
	private static final long serialVersionUID = 3220211786969738916L;

	public ValueNotFoundException(String message){
		super(String.format("No comparevalue found with order: %s", message));
	}
}
