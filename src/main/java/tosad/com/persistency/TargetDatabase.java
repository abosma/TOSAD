package tosad.com.persistency;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="target_database")
public class TargetDatabase implements Serializable {
	
	private int id;
	private String name;
	private String username;
	private String password;
	private String connection;
	private String type;
	
	@Id
	@Column(updatable = false, name = "target_id", nullable = false, unique = true)
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(updatable = true, name = "name", nullable = true, length = 250)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Column(updatable = true, name = "username", nullable = true, length = 250)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(updatable = true, name = "password", nullable = true, length = 250)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(updatable = true, name = "connection", nullable = true, length = 250)
	public String getConnection() {
		return connection;
	}

	public void setConnection(String connection) {
		this.connection = connection;
	}

	@Column(updatable = true, name = "type", nullable = true, length = 250)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
