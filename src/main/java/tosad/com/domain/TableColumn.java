package tosad.com.domain;

public class TableColumn {
	private String type;
	private String name;

	public TableColumn (String ty, String na){
	type = ty;
	name = na;
	}
	
	public String getName(){
		return name;
	}
	public String getType(){
		return type;
	}

}
