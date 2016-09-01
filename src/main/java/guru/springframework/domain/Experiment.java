package guru.springframework.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Experiment {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long experiment_id;
	
	@ManyToOne
	@JoinColumn(name="project_id")
	private Project project;
	
	@OneToMany(mappedBy="experiment")
	private Set<DataFile> dataFiles;

	public long getExperiment_id() {
		return experiment_id;
	}

	public void setExperiment_id(long experiment_id) {
		this.experiment_id = experiment_id;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Set<DataFile> getDataFiles() {
		return dataFiles;
	}

	public void setDataFiles(Set<DataFile> dataFiles) {
		this.dataFiles = dataFiles;
	}
	
	
}
