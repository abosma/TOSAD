package tosad.com.generator;

public class UnknownKeywordException extends Exception {

	private static final long serialVersionUID = 7879241053364903928L;

	public UnknownKeywordException(String message){
		super(String.format("Keyword '%s' nor recognized", message));
	}
}
