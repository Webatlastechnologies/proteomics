package guru.springframework.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class DataFile {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long data_file_id;
	
	@ManyToOne
	@JoinColumn(name="experiment_id")
	private Experiment experiment;
	
	public long getData_file_id() {
		return data_file_id;
	}

	public void setData_file_id(long data_file_id) {
		this.data_file_id = data_file_id;
	}
}
