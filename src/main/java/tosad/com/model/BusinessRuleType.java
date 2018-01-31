package tosad.com.model;

import tosad.com.model.enums.ValueType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "business_rule_types")
public class BusinessRuleType implements Serializable {

	private static final long serialVersionUID = 9157483833030449230L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "br_type_generator")
	@SequenceGenerator(name="br_type_generator", sequenceName = "business_rule_type_seq", allocationSize=50)
	private int id;

	@Column(name = "name", nullable = false, length = 250)
	private String name;

	@Column(name = "allowed_value_types", nullable = false, length = 250)
	private String allowedValueTypes;

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

	public Set<ValueType> getAllowedValueTypes() {
		String[] sValues = allowedValueTypes.split(";");
		Set<ValueType> values = new HashSet<ValueType>();
		
		for (int i = 0; i < sValues.length; i++) 
			values.add( ValueType.valueOf(sValues[i]) );
		
		return values;
	}

	public void setAllowedValueTypes(Set<ValueType> valueTypes) {
		String[] tmp = new String[valueTypes.size()];
		int iterationCount = 0;
		for(ValueType type : valueTypes)
			tmp[iterationCount++] = type.toString();
		
		this.allowedValueTypes = String.join(";", tmp);
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
	@Override
	public String toString() {
		return "BusinessRuleType [id=" + this.id + ", name=" + this.name + "]";
	}	
}
