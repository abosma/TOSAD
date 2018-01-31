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

@Entity
@Table(name = "business_rule_types")
public class BusinessRuleType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9157483833030449230L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "br_type_generator")
	@SequenceGenerator(name="br_type_generator", sequenceName = "br_type_seq", allocationSize=50)
	private int id;

	@Column(name = "name", nullable = false, length = 250)
	private String name;

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "code", nullable = false, length = 255)
	private String code;
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "business_rule_type_operator", joinColumns = { @JoinColumn(name = "businessrule_type_id", insertable = true, updatable=false) }, inverseJoinColumns = {
			@JoinColumn(name = "operator_type_id",  referencedColumnName ="id") })
	private Set<Operator> operators = new HashSet<>();

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

	public Set<Operator> getOperators() {
		return operators;
	}

	public void setOperators(Set<Operator> operators) {
		this.operators = operators;
	}

	public void addOperator(Operator operator) {
		this.operators.add(operator);
	}
	
	@Override
	public String toString() {
		return "BusinessRuleType [id=" + this.id + ", name=" + this.name + ", operators=" + this.operators + "]";
	}	
}
