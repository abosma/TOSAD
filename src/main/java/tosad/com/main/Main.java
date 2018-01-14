package tosad.com.main;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;

import tosad.com.dao.TableDao;
import tosad.com.domain.Table;
import tosad.com.domain.TableColumn;

public class Main {

	public static void main(String[] argv) throws SQLException {

		TableDao dao = new TableDao();

		ArrayList<Table> alltables = new ArrayList<Table>();
		alltables = dao.getAllTables();

		System.out.println(alltables.size());

		JsonArrayBuilder arrayTables = Json.createArrayBuilder();

		for (Table t : alltables) {
			System.out.println(t.getName());
			JsonObjectBuilder ta = Json.createObjectBuilder().add("tablename", t.getName());
			JsonArrayBuilder arrayColumns = Json.createArrayBuilder();

			for (TableColumn column : t.getAllcolumns()) {

				JsonObjectBuilder co = Json.createObjectBuilder().add("columnname", column.getName());
				co.add("type", column.getType());
				arrayColumns.add(co);
			}
			ta.add("Columns", arrayColumns);
			arrayTables.add(ta);

		}

		System.out.println(arrayTables.build().toString());

	}
}
