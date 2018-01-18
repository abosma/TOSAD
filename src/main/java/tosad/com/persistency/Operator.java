package tosad.com.persistency;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="operator")
public class Operator implements Serializable {
	
	private int id;
	private String name;
	private String value;
	private String number_of_values;
	
	@Id
	@Column(updatable = false, name = "id", nullable = false)
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(updatable = true, name = "name", nullable = true, length = 250)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(updatable = true, name = "value", nullable = true, length = 250)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(updatable = true, name = "number_of_values", nullable = true, length = 250)
	public String getNumber_of_values() {
		return number_of_values;
	}

	public void setNumber_of_values(String number_of_values) {
		this.number_of_values = number_of_values;
	}
}
