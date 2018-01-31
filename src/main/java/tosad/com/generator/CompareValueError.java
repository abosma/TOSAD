package tosad.com.generator;

public class CompareValueError extends Exception {

	private static final long serialVersionUID = -292943343482323150L;

	public CompareValueError(String message){
		super(String.format("Cannot evaluate CompareValue: %s", message));
	}
}
