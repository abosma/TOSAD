package tosad.com.persistency;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

@Entity
@Table(name="businessruletype")
public class BusinessRuleType implements Serializable {
	
	private int id;
	private String name;
	private Set<Operator> operators = new HashSet<Operator>(0);
	
	@Id
	@Column(updatable = false, name = "businessruletype_id", nullable = false, unique = true)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(updatable = true, name = "column", nullable = false, length = 250)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "businessruletype_operator", 
			   joinColumns = { @JoinColumn(name = "businessruletype_id", nullable = false, updatable = false) },
			   inverseJoinColumns = { @JoinColumn(name = "operator_id", nullable = false, updatable = false) })
	public Set<Operator> getOperators() {
		return operators;
	}
	public void setOperators(Set<Operator> operators) {
		this.operators = operators;
	}
	
	
}
