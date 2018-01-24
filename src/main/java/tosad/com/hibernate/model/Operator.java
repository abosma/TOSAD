package tosad.com.hibernate.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "operators")
public class Operator implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5058409315298659885L;

	@Id
	@GeneratedValue
	private int id;

	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@Column(name = "value", nullable = false, length = 255)
	private String value;

	@Column(name = "number_of_values", nullable = false)
	private int numberOfValues;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "business_rule_type_operator", joinColumns = { @JoinColumn(name = "id", insertable = false, updatable = false) }, inverseJoinColumns = {
			@JoinColumn(name = "id", insertable = false, updatable = false) })
	private Set<BusinessRuleType> businessRuleTypes = new HashSet<>();

	public int getId() {
		return id;
	}

	public Operator setId(int id) {
		this.id = id;
		
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getNumberOfValues() {
		return numberOfValues;
	}

	public void setNumberOfValues(int numberOfValues) {
		this.numberOfValues = numberOfValues;
	}

	public Set<BusinessRuleType> getBusinessRuleTypes() {
		return businessRuleTypes;
	}

	public void setBusinessRuleTypes(Set<BusinessRuleType> businessRuleTypes) {
		this.businessRuleTypes = businessRuleTypes;
	}

	@Override
	public String toString() {
		return "Operator [id=" + this.id + ", name=" + this.name + ", value=" + this.value + ", numberOfValues="
				+ this.numberOfValues + "]";
	}

	
}
