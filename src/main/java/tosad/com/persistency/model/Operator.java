package tosad.com.persistency.model;

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
@Table(name="operator")
public class Operator implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5058409315298659885L;
	
	@Id
	@GeneratedValue
	private int id;

	@Column(name="name", nullable = false, length=255)
	private String name;
	
	@Column(name="value", nullable = false, length=255)
	private String value;
	
	@Column(name="number_of_values", nullable = false)
	private int numberOfValues;
	

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(
		name = "business_rule_type_operator",
		joinColumns = { @JoinColumn(name = "id") },
		inverseJoinColumns = { @JoinColumn(name="id") } 
	)
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


	public Set<Operator> getOperators() {
		return operators;
	}


	public void setOperators(Set<Operator> operators) {
		this.operators = operators;
	}
	
	
}
