package tosad.com.persistency;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="hibernate_test")
public class Test implements Serializable {
	Long id;
	
	@Id
	@Column(updatable = true, name = "testLong", nullable = true, length = 50)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
}
