package tosad.com.persistency.model;

import java.io.Serializable;

import javax.persistence.*;


@Entity
@Table(name="target_database_type")
public class TargetDatabaseType implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1403812530125696107L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name="type", nullable = false, length = 50)
	private String name;

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
}
