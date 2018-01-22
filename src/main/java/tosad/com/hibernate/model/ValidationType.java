package tosad.com.hibernate.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ValidationType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4492094324932546120L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name="validation_type")
	private String validationType;
	
	@Column(name="execution_type")
	private String executionType;
	
	@Column(name="execution_level")
	private String executionLevel;
	
	@Column(name="trigger_level")
	private String triggerLevel;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getValidationType() {
		return validationType;
	}

	public void setValidationType(String validationType) {
		this.validationType = validationType;
	}

	public String getExecutionType() {
		return executionType;
	}

	public void setExecutionType(String executionType) {
		this.executionType = executionType;
	}

	public String getExecutionLevel() {
		return executionLevel;
	}

	public void setExecutionLevel(String executionLevel) {
		this.executionLevel = executionLevel;
	}

	public String getTriggerLevel() {
		return triggerLevel;
	}

	public void setTriggerLevel(String triggerLevel) {
		this.triggerLevel = triggerLevel;
	}
}
