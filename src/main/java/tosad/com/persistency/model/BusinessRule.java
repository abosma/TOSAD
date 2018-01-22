package tosad.com.persistency.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="business_rules")
public class BusinessRule implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1293391282912092618L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column( name = "name", nullable = false, length = 255)
	private String name;
	
	@Column( name = "error_message", nullable = true, length = 4000 )
	private String errorMessage;
	
	@Column( name = "example", nullable = true, length = 4000 )
	private String example;
	
	@Column( name = "explanation", nullable = false, length = 4000 )
	private String explanation;
	
	@Column( name = "referenced_column", nullable = false, length = 255 )
	private String referencedColumn;
	
	@Column( name = "referenced_table", nullable = false, length = 255 )
	private String referencedTable;
	
	@ManyToOne
	@JoinColumn(name="id")
	private Trigger trigger;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="id")
	private ValidationType validationType;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="id")
	private Operator operator;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="id")
	private BusinessRuleType businessRuleType;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="id")
	private TargetDatabase targetDatabase;

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

	public Trigger getTrigger() {
		return trigger;
	}

	public void setTrigger(Trigger trigger) {
		this.trigger = trigger;
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

	public ValidationType getValidationType() {
		return validationType;
	}

	public void setValidationType(ValidationType validationType) {
		this.validationType = validationType;
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
}
