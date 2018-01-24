package tosad.com.hibernate.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "target_database_types")
public class TargetDatabaseType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1403812530125696107L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "type", nullable = false, length = 50)
	private String name;

	@OneToMany(mappedBy = "targetDatabaseType")
	private Set<RuleTemplate> templates = new HashSet<RuleTemplate>();
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "TargetDatabaseType [id=" + this.id + ", name=" + this.name + "]";
	}

	public Set<RuleTemplate> getTemplates() {
		return this.templates;
	}
	
	public boolean addTemplate(RuleTemplate ruleTemplate){
		return this.templates.add(ruleTemplate);
	}

	public void setTemplates(Set<RuleTemplate> templates) {
		this.templates = templates;
	}
}
