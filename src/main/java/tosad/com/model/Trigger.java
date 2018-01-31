package tosad.com.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table( name = "triggers" )
public class Trigger implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2576986450271109183L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trigger_generator")
	@SequenceGenerator(name="trigger_generator", sequenceName = "trigger_seq", allocationSize=50)
	private int id;

	@Column(name = "execution_type", nullable = false, length = 255)
	private String executionType;

	@Column(name = "execution_type_translations", nullable = true, length = 4000)
	private String executionTypeTranslations;

	@Column(name = "execution_level", nullable = false, length = 255)
	private String executionLevel;

	@Column(name = "execution_level_translations", nullable = true, length = 4000)
	private String executionLevelTranslations;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getExecutionType() {
		return executionType;
	}

	public void setExecutionType(String executionType) {
		this.executionType = executionType;
	}

	public String getExecutionTypeTranslations() {
		return executionTypeTranslations;
	}

	public void setExecutionTypeTranslations(String executionTypeTranslations) {
		this.executionTypeTranslations = executionTypeTranslations;
	}

	public String getExecutionLevel() {
		return executionLevel;
	}

	public void setExecutionLevel(String executionLevel) {
		this.executionLevel = executionLevel;
	}

	public String getExecutionLevelTranslations() {
		return executionLevelTranslations;
	}

	public void setExecutionLevelTranslations(String executionLevelTranslations) {
		this.executionLevelTranslations = executionLevelTranslations;
	}

	@Override
	public String toString() {
		return "Trigger [id=" + this.id + ", executionType=" + this.executionType + ", executionTypeTranslations="
				+ this.executionTypeTranslations + ", executionLevel=" + this.executionLevel
				+ ", executionLevelTranslations=" + this.executionLevelTranslations + "]";
	}
	
	
}
