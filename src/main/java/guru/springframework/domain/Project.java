package guru.springframework.domain;

import java.sql.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long project_id;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@OneToMany(mappedBy="project")
	private Set<Experiment> experiments;
	
	@ManyToMany(cascade=CascadeType.ALL)  
	@JoinTable(name="user_project", joinColumns=@JoinColumn(name="project_id"), inverseJoinColumns=@JoinColumn(name="user_id"))  
	private Set<User> users;
	 
	private String projectName;
	
	private Date createdDate;
	
	private String description;
	
	private boolean isArchive;
	
	private boolean isEditable;
	
	private boolean canDelete;
	
	private int noOfSharedUsers;
	 
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<Experiment> getExperiments() {
		return experiments;
	}

	public void setExperiments(Set<Experiment> experiments) {
		this.experiments = experiments;
	}

	public long getProject_id() {
		return project_id;
	}

	public void setProject_id(long project_id) {
		this.project_id = project_id;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isArchive() {
		return isArchive;
	}

	public void setArchive(boolean isArchive) {
		this.isArchive = isArchive;
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	public boolean isCanDelete() {
		return canDelete;
	}

	public void setCanDelete(boolean canDelete) {
		this.canDelete = canDelete;
	}

	public int getNoOfSharedUsers() {
		return noOfSharedUsers;
	}

	public void setNoOfSharedUsers(int noOfSharedUsers) {
		this.noOfSharedUsers = noOfSharedUsers;
	}
}
