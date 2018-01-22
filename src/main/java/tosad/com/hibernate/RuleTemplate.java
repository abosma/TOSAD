package tosad.com.hibernate;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class RuleTemplate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6054864025894349903L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name="template", nullable=false, length=4000)
	private String template;
	
	@ManyToOne
	@JoinColumn(name="id")
	private TargetDatabaseType targetDatabaseType;
	
	@ManyToOne
	@JoinColumn(name="id")
	private BusinessRuleType businessRuleType;
	
	@ManyToOne
	@JoinColumn(name="id")
	private ValidationType validationType;
	
	@ManyToOne
	@JoinColumn(name="id")
	private Operator operator;

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

	public TargetDatabaseType getTargetDatabaseType() {
		return targetDatabaseType;
	}

	public void setTargetDatabaseType(TargetDatabaseType targetDatabaseType) {
		this.targetDatabaseType = targetDatabaseType;
	}

	public BusinessRuleType getBusinessRuleType() {
		return businessRuleType;
	}

	public void setBusinessRuleType(BusinessRuleType businessRuleType) {
		this.businessRuleType = businessRuleType;
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
}
