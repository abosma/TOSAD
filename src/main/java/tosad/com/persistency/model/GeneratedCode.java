package tosad.com.persistency.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "generated_code")
public class GeneratedCode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4862358544941898382L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(updatable = false, name = "status", nullable = false, unique = true)
	private int status;

	@Column(updatable = false, name = "code", nullable = false, unique = true, length = 500)
	private String code;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id")
	private BusinessRule businessRule;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public BusinessRule getBusinessRule() {
		return businessRule;
	}

	public void setBusinessRule(BusinessRule businessRule) {
		this.businessRule = businessRule;
	}
}
