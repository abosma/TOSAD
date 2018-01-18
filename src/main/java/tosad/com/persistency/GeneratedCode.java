package tosad.com.persistency;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="generated_code")
public class GeneratedCode implements Serializable {
	
	private int id;
	private int status;
	private String code;
	private int businessrule_id;
	
	@Id
	@Column(updatable = false, name = "code_id", nullable = false, unique = true)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(updatable = false, name = "status", nullable = false, unique = true)
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	@Column(updatable = false, name = "code", nullable = false, unique = true, length = 500)
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "businessrule_id")
	public int getBusinessrule_id() {
		return businessrule_id;
	}
	public void setBusinessrule_id(int businessrule_id) {
		this.businessrule_id = businessrule_id;
	}
}
