package tosad.com.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "compare_values")
public class CompareValue implements Serializable {

	private static final long serialVersionUID = -3559038127062256567L;	

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comp_val_generator")
	@SequenceGenerator(name="comp_val_generator", sequenceName = "compare_value_seq", allocationSize=50)
	private int id;

	@Column(name = "table_name", nullable = true, length = 255)
	private String table;

	@Column(name = "column_name", nullable = true, length = 255)
	private String column;

	@Column(name = "value_", nullable = true, length = 255)
	private String value;
	
	@Column(name= "ordertype", nullable = true )
	private int order;
	
	@ManyToOne
	@JoinColumn(name = "businessrule_id", insertable = true, updatable=false)
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

	public int getOrder() {
		return this.order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "CompareValue [id=" + this.id + ", table=" + this.table + ", column=" + this.column + ", value="
				+ this.value + ", order=" + this.order + "]";
	}
}
