package tosad.com.hibernate.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "constrains")
public class Constraint implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4419857291887922866L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	public int getId() {
		return this.id;
	}

	@Override
	public String toString() {
		return "Constraint [id=" + this.id + "]";
	}

	
}
