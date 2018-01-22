package tosad.com.hibernate.model;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "target_database")
public class TargetDatabase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9141646972166988519L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@Column(name = "name", nullable = false, length = 255)
	private String username;

	@Column(name = "name", nullable = false, length = 255)
	private String password;

	@Column(name = "name", nullable = false, length = 255)
	private String connection;

	@ManyToOne
	@JoinColumn(name = "id")
	private TargetDatabaseType targetDatabaseType;

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConnection() {
		return connection;
	}

	public void setConnection(String connection) {
		this.connection = connection;
	}

	public TargetDatabaseType getTargetDatabaseType() {
		return targetDatabaseType;
	}

	public void setTargetDatabaseType(TargetDatabaseType targetDatabaseType) {
		this.targetDatabaseType = targetDatabaseType;
	}
}
