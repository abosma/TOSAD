package tosad.com.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import tosad.com.model.enums.Amount;

@Entity
@Table(name = "operators")
public class Operator implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5058409315298659885L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "operator_generator")
	@SequenceGenerator(name="operator_generator", sequenceName = "operator_seq", allocationSize=50)
	private int id;

	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@Column(name = "code", nullable = false, length = 255)
	private String code;

	@Column(name = "number_of_values", nullable = true)
	private String amountOfValues;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "business_rule_type_operator", joinColumns = { @JoinColumn(name = "operator_type_id", insertable = true, updatable = false) }, inverseJoinColumns = {
			@JoinColumn(name = "businessrule_type_id", referencedColumnName ="id") })
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
	
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Amount getAmountOfValues() {
		return Amount.valueOf(this.amountOfValues);
	}

	public void setAmountOfValues(Amount amount) {
		this.amountOfValues = amount.toString();
	}

	public Set<BusinessRuleType> getBusinessRuleTypes() {
		return businessRuleTypes;
	}

	public void setBusinessRuleTypes(Set<BusinessRuleType> businessRuleTypes) {
		this.businessRuleTypes = businessRuleTypes;
	}

	@Override
	public String toString() {
		return "Operator [id=" + this.id + ", name=" + this.name + ", code=" + this.code + ", amountOfValues="
				+ this.amountOfValues + ", businessRuleTypes=" + this.businessRuleTypes + "]";
	}	
}
