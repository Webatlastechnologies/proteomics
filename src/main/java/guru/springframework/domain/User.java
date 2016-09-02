package guru.springframework.domain;


import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {

	public User() {
	}

	public User(String firstName, String lastName, String email, String username, String password, String role) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.username = username;
		this.password = password;
		this.role = role;
	}
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long user_id;
	
	private String firstName;
	private String lastName;
	private String email;
	private String username;
	private String password;
	private String role;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="lab_id")
	@JsonIgnore
	private Lab lab;
	
	@OneToMany(mappedBy="user")
	@JsonIgnore
	private Set<LabDatabase> labDatabases;
	
	@OneToMany(mappedBy="user")
	@JsonIgnore
	private Set<Project> projects;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
	public Set<Project> sharedProjects;
	
	public Lab getLab() {
		return lab;
	}

	public void setLab(Lab lab) {
		this.lab = lab;
	}
	
	
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return String.format("Person[id=%d, firstName='%s', lastName='%s']", user_id, firstName, lastName);
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public Set<LabDatabase> getLabDatabases() {
		return labDatabases;
	}

	public void setLabDatabases(Set<LabDatabase> labDatabases) {
		this.labDatabases = labDatabases;
	}

	

}
