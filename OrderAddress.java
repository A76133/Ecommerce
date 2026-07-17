package in.main.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class OrderAddress {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column
	private Integer id;
     @Column
	private String firstName;
     @Column
	private String lastName;
     
     private String email;
     @Column	
	private String mobileNo;
     @Column
	private String address;
     @Column
	private String city;
     @Column
	private String state;
     public String getEmail() {
		return email;
	}
	 public void setEmail(String email) {
		 this.email = email;
	 }
	 @Column
	private String pincode;
	 public Integer getId() {
		 return id;
	 }
	 public void setId(Integer id) {
		 this.id = id;
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
	 public String getMobileNo() {
		 return mobileNo;
	 }
	 public void setMobileNo(String mobileNo) {
		 this.mobileNo = mobileNo;
	 }
	 public String getAddress() {
		 return address;
	 }
	 public void setAddress(String address) {
		 this.address = address;
	 }
	 public String getCity() {
		 return city;
	 }
	 public void setCity(String city) {
		 this.city = city;
	 }
	 public String getState() {
		 return state;
	 }
	 public void setState(String state) {
		 this.state = state;
	 }
	 public String getPincode() {
		 return pincode;
	 }
	 public void setPincode(String pincode) {
		 this.pincode = pincode;
	 }
	
	
	
	
}
