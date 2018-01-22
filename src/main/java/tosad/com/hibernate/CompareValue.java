package tosad.com.hibernate;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="compare_values")
public class CompareValue implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3559038127062256567L;

	@Id
	@GeneratedValue
	private int id;
	
	@Column(name = "table", nullable = true, length = 255)
	private String table;
	
	@Column(name = "column", nullable = true, length = 255)
	private String column;
	
	@Column(name = "value", nullable = true, length = 255)
	private String value;

	@ManyToOne
	@JoinColumn(name="id")
	private BusinessRule businessRule;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public BusinessRule getBusinessRule() {
		return businessRule;
	}

	public void setBusinessRule(BusinessRule businessRule) {
		this.businessRule = businessRule;
	}
}
