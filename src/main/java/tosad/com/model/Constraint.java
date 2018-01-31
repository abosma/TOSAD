package tosad.com.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "constraints")
public class Constraint implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4419857291887922866L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "constraint_generator")
	@SequenceGenerator(name="constraint_generator", sequenceName = "constraint_seq", allocationSize=50)
	private int id;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Constraint [id=" + this.id + "]";
	}

	
}
