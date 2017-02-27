package pf.user;


import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;

@Entity 
@Table (name="user")
public class UserEntity {

	@Id  //primary key
	private String id;
	private String email;
	private String password;
	private int verified;
	@Column(name="verification_key")
	private String verificationKey;
	@Column(name="creation_date")
	private String creationDate;
	@Column(name="reset_password_key")
	private String resetPasswordKey;
	private Double usd_rate;
	private Double sar_rate;
	

	//Default constructor is required by hibernate
	public UserEntity() {
		
	}
	
	//Essential properties only
	public UserEntity(String id, String email, String password) {
		this.id = id;
		this.email = email;
		this.password = password;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(new Date());		
		this.creationDate = today;
		verified = 0;
		usd_rate = 1.0;
		sar_rate = 1.0;
	}

	///////////////////////////////////////////////////////////////////////////////////

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public int getVerified() {
		return verified;
	}

	public void setVerified(int verified) {
		this.verified = verified;
	}

	public String getVerification_key() {
		return verificationKey;
	}

	public void setVerification_key(String verification_key) {
		this.verificationKey = verification_key;
	}

	public String getCreation_date() {
		return creationDate;
	}

	public void setCreation_date(String creation_date) {
		this.creationDate = creation_date;
	}

	public String getReset_password_key() {
		return resetPasswordKey;
	}

	public void setReset_password_key(String reset_password_key) {
		this.resetPasswordKey = reset_password_key;
	}

	public Double getUsd_rate() {
		return usd_rate;
	}

	public void setUsd_rate(Double usd_rate) {
		this.usd_rate = usd_rate;
	}

	public Double getSar_rate() {
		return sar_rate;
	}

	public void setSar_rate(Double sar_rate) {
		this.sar_rate = sar_rate;
	}

	public String getDate() {
		return creationDate;
	}

	public void setDate(String date) {
		this.creationDate = date;
		
	}
}
