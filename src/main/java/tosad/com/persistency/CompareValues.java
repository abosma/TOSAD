package tosad.com.persistency;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="comparevalues")
public class CompareValues implements Serializable {
	
	private int id;
	private int bRuleId;
	private String column;
	private String table;
	private String attribute;
	private String single_value;
	private String min_value;
	private String max_value;
	
	@Id
	@Column(updatable = false, name = "id", nullable = false)
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "businessrule_id")
	public int getbRuleId() {
		return bRuleId;
	}
	
	public void setbRuleId(int bRuleId) {
		this.bRuleId = bRuleId;
	}
	
	@Column(updatable = true, name = "column", nullable = false)
	public String getColumn() {
		return column;
	}
	
	public void setColumn(String column) {
		this.column = column;
	}
	
	@Column(updatable = true, name = "table", nullable = false)
	public String getTable() {
		return table;
	}
	
	public void setTable(String table) {
		this.table = table;
	}
	
	@Column(updatable = true, name = "attribute", nullable = false)
	public String getAttribute() {
		return attribute;
	}
	
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	
	@Column(updatable = true, name = "single_value", nullable = false)
	public String getSingle_value() {
		return single_value;
	}
	
	public void setSingle_value(String single_value) {
		this.single_value = single_value;
	}
	
	@Column(updatable = true, name = "min_value", nullable = false)
	public String getMin_value() {
		return min_value;
	}
	
	public void setMin_value(String min_value) {
		this.min_value = min_value;
	}
	
	@Column(updatable = true, name = "max_value", nullable = false)
	public String getMax_value() {
		return max_value;
	}
	
	public void setMax_value(String max_value) {
		this.max_value = max_value;
	}
}
