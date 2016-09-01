package guru.springframework.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class LabDatabase {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long database_id;

	public long getDatabase_id() {
		return database_id;
	}

	public void setDatabase_id(long database_id) {
		this.database_id = database_id;
	}
	
	@ManyToOne
	@JoinColumn(name="lab_id")
	private Lab lab;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@OneToOne	
	@JoinColumn(name="ORG_ID", nullable=true, insertable=true, updatable=true)
	private Organism organism;
	
	@OneToOne	
	@JoinColumn(name="SRC_ID", nullable=true, insertable=true, updatable=true)
	private Source source;
	
	@Column(name="RELEASED_DATE")
	private Date releasedDate;
	
	@Column(name="VERSION")
	private String version;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="GEN_REV_SEQ")
	@Enumerated(EnumType.STRING)
	private YesNo genRevSeq;
	
	@Column(name="ADD_CON_PROTEIN")
	@Enumerated(EnumType.STRING)
	private YesNo addConProtein;
	
	@Column(name="FILE_PATH")
	private String filePath;
	
	@Column(name="UPLOADED_DATE")
	private Date uploadedDate;
	
	@Column(name="UPLOADED_BY")
	private String uploadedBy;
	
	@Column(name="PROTEIN_NUM")
	private long proteinNum;
	
	@Column(name="FILE_SIZE")
	private long sizeInKb;

	public Lab getLab() {
		return lab;
	}

	public void setLab(Lab lab) {
		this.lab = lab;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Organism getOrganism() {
		return organism;
	}

	public Source getSource() {
		return source;
	}

	public Date getReleasedDate() {
		return releasedDate;
	}

	public String getVersion() {
		return version;
	}

	public String getDescription() {
		return description;
	}

	public YesNo getGenRevSeq() {
		return genRevSeq;
	}

	public YesNo getAddConProtein() {
		return addConProtein;
	}

	public String getFilePath() {
		return filePath;
	}

	public Date getUploadedDate() {
		return uploadedDate;
	}

	public String getUploadedBy() {
		return uploadedBy;
	}

	public long getProteinNum() {
		return proteinNum;
	}

	public long getSizeInKb() {
		return sizeInKb;
	}
}
