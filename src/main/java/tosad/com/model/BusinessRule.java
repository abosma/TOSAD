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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "business_rules")
public class BusinessRule implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1293391282912092618L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "businessrule_generator")
	@SequenceGenerator(name="businessrule_generator", sequenceName = "business_rule_seq", allocationSize=50)
	private int id;

	@Column(name = "name", nullable = true, length = 255)
	private String name;

	@Column(name = "error_message", nullable = true, length = 4000)
	private String errorMessage;

	@Column(name = "example", nullable = true, length = 4000)
	private String example;

	@Column(name = "explanation", nullable = true, length = 4000)
	private String explanation;

	@Column(name = "referenced_column", nullable = false, length = 255)
	private String referencedColumn;

	@Column(name = "referenced_table", nullable = false, length = 255)
	private String referencedTable;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn( insertable = true, updatable = true )
	private BusinessRuleType businessRuleType;
	

	@ManyToOne
	@JoinColumn(name = "trigger_id", insertable = true, updatable = false)
	private Trigger trigger;

	@ManyToOne
	@JoinColumn(name = "constraint_id", insertable = true, updatable = false)
	private Constraint constraint;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "operator_id", insertable = true, updatable = false)
	private Operator operator;



	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "targetDatabase_id", insertable = true, updatable = false)
	private TargetDatabase targetDatabase;

	@OneToMany(mappedBy = "businessRule")
	private Set<CompareValue> compareValues = new HashSet<CompareValue>();

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

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getExample() {
		return example;
	}

	public void setExample(String example) {
		this.example = example;
	}

	public String getExplanation() {
		return explanation;
	}

	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}

	public String getReferencedColumn() {
		return referencedColumn;
	}

	public void setReferencedColumn(String referencedColumn) {
		this.referencedColumn = referencedColumn;
	}

	public String getReferencedTable() {
		return referencedTable;
	}

	public void setReferencedTable(String referencedTable) {
		this.referencedTable = referencedTable;
	}
	
	public Trigger getTrigger() {
		return this.trigger;
	}

	public Constraint getConstraint() {
		return this.constraint;
	}

	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
	}

	public void setConstraint(Constraint constraint) {
		this.constraint = constraint;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public BusinessRuleType getBusinessRuleType() {
		return businessRuleType;
	}

	public void setBusinessRuleType(BusinessRuleType businessRuleType) {
		this.businessRuleType = businessRuleType;
	}

	public TargetDatabase getTargetDatabase() {
		return targetDatabase;
	}

	public void setTargetDatabase(TargetDatabase targetDatabase) {
		this.targetDatabase = targetDatabase;
	}
	
	public Set<CompareValue> getCompareValues() {
		return this.compareValues;
	}

	public void setCompareValues(Set<CompareValue> compareValues) {
		this.compareValues = compareValues;
	}
	
	public boolean addCompareValue(CompareValue compareValue){
		return this.compareValues.add(compareValue);
	}

	@Override
	public String toString() {
		return "BusinessRule [id=" + this.id + ", name=" + this.name + ", errorMessage=" + this.errorMessage
				+ ", example=" + this.example + ", explanation=" + this.explanation + ", referencedColumn="
				+ this.referencedColumn + ", referencedTable=" + this.referencedTable + ", trigger=" + this.trigger
				+ ", constraint=" + this.constraint + ", operator=" + this.operator + ", businessRuleType="
				+ this.businessRuleType + ", targetDatabase=" + this.targetDatabase + ", compareValues="
				+ this.compareValues + "]";
	}
}




