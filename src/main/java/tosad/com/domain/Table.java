package tosad.com.domain;

import java.util.ArrayList;

public class Table {
	private String name;
	private ArrayList<TableColumn> allcolumns = new ArrayList<TableColumn>();
	
	public Table(String na){
		name = na;
	}
	
	public ArrayList<TableColumn> getAllcolumns() {
		return allcolumns;
	}

	public void setAllcolumns(ArrayList<TableColumn> allcolumns) {
		this.allcolumns = allcolumns;
	}
	
	public void addColumns(TableColumn tc) {
		this.allcolumns.add(tc);
	}



	
	
	

}
