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

import tosad.com.model.TargetDatabaseType;

@Entity
@Table(name = "rule_templates")
public class RuleTemplate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6054864025894349903L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rule_template_generator")
	@SequenceGenerator(name="rule_template_generator", sequenceName = "rule_template_seq", allocationSize=50)
	private int id;

	@Column(name = "template", nullable = false, length = 4000)
	private String template;

	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@ManyToOne
	@JoinColumn(name = "targetDatabaseType_id", insertable = true, updatable = false)
	private TargetDatabaseType targetDatabaseType;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public TargetDatabaseType getTargetDatabaseType() {
		return targetDatabaseType;
	}

	public void setTargetDatabaseType(TargetDatabaseType targetDatabaseType) {
		this.targetDatabaseType = targetDatabaseType;
	}

	@Override
	public String toString() {
		return "RuleTemplate [id=" + this.id + ", name=" + this.name + ", targetDatabaseType=" + this.targetDatabaseType
				+ "]";
	}
	
	
}

