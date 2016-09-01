package guru.springframework.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Lab {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long lab_id;
	
	@OneToMany(mappedBy="lab")
	private Set<User> users;
	
	@OneToMany(mappedBy="lab")
	private Set<LabDatabase> labDatabases;
	
	
	public long getLab_id() {
		return lab_id;
	}

	public void setLab_id(long lab_id) {
		this.lab_id = lab_id;
	}

	public Set<LabDatabase> getLabDatabases() {
		return labDatabases;
	}

	public void setLabDatabases(Set<LabDatabase> labDatabases) {
		this.labDatabases = labDatabases;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	
}
