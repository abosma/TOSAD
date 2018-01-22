package tosad.com.persistency.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Trigger implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2576986450271109183L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column( name = "type", nullable = false, length = 255)
	private String type;
	
	@Column( name = "type_translation", nullable = false, length = 4000)
	private String typeTranslation;
	
	@Column( name = "execution_type", nullable = false, length = 255)
	private String executionType;
	
	@Column( name = "execution_type_translations", nullable = false, length = 4000)
	private String executionTypeTranslations;
	
	@Column( name = "execution_level", nullable = false, length = 255)
	private String executionLevel;
	
	@Column( name = "execution_level_translations", nullable = false, length = 4000)
	private String executionLevelTranslations;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeTranslation() {
		return typeTranslation;
	}

	public void setTypeTranslation(String typeTranslation) {
		this.typeTranslation = typeTranslation;
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
	
	
}
