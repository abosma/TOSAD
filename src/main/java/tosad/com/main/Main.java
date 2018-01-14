package tosad.com.main;

import java.sql.SQLException;
import java.util.ArrayList;

import tosad.com.dao.TableDao;
import tosad.com.domain.Table;


public class Main {

	public static void main(String[] argv) throws SQLException {
		
		TableDao dao = new TableDao();
		
		ArrayList<Table> alltables  = new ArrayList<Table>();
		alltables = dao.getAllTables();
		for(Table t: alltables){
			System.out.println(t.getAllcolumns().size());
			
		}
	}
}
