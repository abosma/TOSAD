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
@Table(name="businessrule")
public class BusinessRule implements Serializable {
	
	private int id;
	private String name;
	private String errormessage;
	private String error_example;
	private String error_explanation;
	private String validation_type;
	private String execution_level;
	private String execution_type;
	private String trigger_type;
	private String referenced_column;
	private String referenced_table;
	private int operator_id;
	private int ruletype_id;
	private int target_id;
	
	@Id
	@Column(updatable = false, name = "businessrule_id", nullable = false, unique = true)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(updatable = true, name = "name", nullable = false, length = 250)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(updatable = true, name = "errormessage", nullable = false, length = 250)
	public String getErrormessage() {
		return errormessage;
	}
	public void setErrormessage(String errormessage) {
		this.errormessage = errormessage;
	}
	
	@Column(updatable = true, name = "error_example", nullable = false, length = 250)
	public String getError_example() {
		return error_example;
	}
	public void setError_example(String error_example) {
		this.error_example = error_example;
	}
	
	@Column(updatable = true, name = "error_explanation", nullable = false, length = 250)
	public String getError_explanation() {
		return error_explanation;
	}
	public void setError_explanation(String error_explanation) {
		this.error_explanation = error_explanation;
	}
	
	@Column(updatable = true, name = "validation_type", nullable = false, length = 250)
	public String getValidation_type() {
		return validation_type;
	}
	public void setValidation_type(String validation_type) {
		this.validation_type = validation_type;
	}
	
	@Column(updatable = true, name = "execution_level", nullable = false, length = 250)
	public String getExecution_level() {
		return execution_level;
	}
	public void setExecution_level(String execution_level) {
		this.execution_level = execution_level;
	}
	
	@Column(updatable = true, name = "execution_type", nullable = false, length = 250)
	public String getExecution_type() {
		return execution_type;
	}
	public void setExecution_type(String execution_type) {
		this.execution_type = execution_type;
	}
	
	@Column(updatable = true, name = "trigger_type", nullable = false, length = 250)
	public String getTrigger_type() {
		return trigger_type;
	}
	public void setTrigger_type(String trigger_type) {
		this.trigger_type = trigger_type;
	}
	
	@Column(updatable = true, name = "referenced_column", nullable = false, length = 250)
	public String getReferenced_column() {
		return referenced_column;
	}
	public void setReferenced_column(String referenced_column) {
		this.referenced_column = referenced_column;
	}
	
	@Column(updatable = true, name = "referenced_table", nullable = false, length = 250)
	public String getReferenced_table() {
		return referenced_table;
	}
	public void setReferenced_table(String referenced_table) {
		this.referenced_table = referenced_table;
	}
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "operator_id")
	public int getOperator_id() {
		return operator_id;
	}
	public void setOperator_id(int operator_id) {
		this.operator_id = operator_id;
	}
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "businessruletype_id")
	public int getRuletype_id() {
		return ruletype_id;
	}
	public void setRuletype_id(int ruletype_id) {
		this.ruletype_id = ruletype_id;
	}
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "target_id")
	public int getTarget_id() {
		return target_id;
	}
	public void setTarget_id(int target_id) {
		this.target_id = target_id;
	}
	
}
