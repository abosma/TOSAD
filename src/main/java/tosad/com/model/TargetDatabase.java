package tosad.com.model;

import java.io.Serializable;
import javax.persistence.*;

import tosad.com.model.TargetDatabaseType;

@Entity
@Table(name = "target_databases")
public class TargetDatabase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9141646972166988519L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "target_db_generator")
	@SequenceGenerator(name="target_db_generator", sequenceName = "target_db_seq", allocationSize=50)
	private int id;

	@Column(name = "name", nullable = false, length = 255)
	private String name;

	@Column(name = "username", nullable = false, length = 255)
	private String username;

	@Column(name = "password", nullable = false, length = 255)
	private String password;

	@Column(name = "connection", nullable = false, length = 255)
	private String connection;

	@ManyToOne
	@JoinColumn(name = "type_id", insertable = true, updatable=false)
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

	@Override
	public String toString() {
		return "TargetDatabase [id=" + this.id + ", name=" + this.name + ", username=" + this.username + ", password="
				+ this.password + ", connection=" + this.connection + ", targetDatabaseType=" + this.targetDatabaseType
				+ "]";
	}
}
