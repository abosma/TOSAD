package tosad.com.persistency;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="validation_type")
public class ValidationType implements Serializable {
	
	private int id;
	private String type;
	private String execution_level;
	private String execution_type;
	private String trigger_type;
	
	@Id
	@Column(updatable = false, name = "id", nullable = false)
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(updatable = true, name = "type", nullable = true, length = 250)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(updatable = true, name = "execution_level", nullable = true, length = 250)
	public String getExecution_level() {
		return execution_level;
	}

	public void setExecution_level(String execution_level) {
		this.execution_level = execution_level;
	}

	@Column(updatable = true, name = "execution_type", nullable = true, length = 250)
	public String getExecution_type() {
		return execution_type;
	}

	public void setExecution_type(String execution_type) {
		this.execution_type = execution_type;
	}

	@Column(updatable = true, name = "trigger_type", nullable = true, length = 250)
	public String getTrigger_type() {
		return trigger_type;
	}

	public void setTrigger_type(String trigger_type) {
		this.trigger_type = trigger_type;
	}
	
}
