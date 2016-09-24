package guru.springframework.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class DataFile {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long data_file_id;
	
	@ManyToOne
	@JoinColumn(name="experiment_id")
	private Experiment experiment;
	
	private String fileName;
	
	private String filePath;
	
	private long fileSize;
	
	@OneToOne(cascade = CascadeType.ALL)
	private DtaFileDetails dtaFileDetails;
	
	public long getData_file_id() {
		return data_file_id;
	}

	public void setData_file_id(long data_file_id) {
		this.data_file_id = data_file_id;
	}

	public Experiment getExperiment() {
		return experiment;
	}

	public void setExperiment(Experiment experiment) {
		this.experiment = experiment;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
}
