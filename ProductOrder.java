package in.main.Model;

import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class ProductOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	 private Integer id;
	
	 private String orderId;
	
	 private LocalDate orderDate;
	@ManyToOne
	 private Product product;

	 private Double price;
	
	 private Integer Quantity;
	 @ManyToOne
	 private UserDetail user;
	 
	 private String status;
	 
	 private String PaymentType;
	 
	 @OneToOne(cascade = CascadeType.ALL)
	 private OrderAddress orderAddress;
	 
	 
	 public OrderAddress getOrderAddress() {
		return orderAddress;
	}

	 public void setOrderAddress(OrderAddress orderAddress) {
		 this.orderAddress = orderAddress;
	 }

	 public Integer getId() {
		return id;
	}

	 public void setId(Integer id) {
		 this.id = id;
	 }

	 public String getOrderId() {
		 return orderId;
	 }

	 public void setOrderId(String orderId) {
		 this.orderId = orderId;
	 }

	 public LocalDate getOrderDate() {
		 return orderDate;
	 }

	 public void setOrderDate(LocalDate orderDate) {
		 this.orderDate = orderDate;
	 }

	 public Product getProduct() {
		 return product;
	 }

	 public void setProduct(Product product) {
		 this.product = product;
	 }

	 public Double getPrice() {
		 return price;
	 }

	 public void setPrice(Double price) {
		 this.price = price;
	 }

	 public Integer getQuantity() {
		 return Quantity;
	 }

	 public void setQuantity(Integer quantity) {
		 Quantity = quantity;
	 }

	 public UserDetail getUser() {
		 return user;
	 }

	 public void setUser(UserDetail user) {
		 this.user = user;
	 }

	 public String getStatus() {
		 return status;
	 }

	 public void setStatus(String status) {
		 this.status = status;
	 }

	 public String getPaymentType() {
		 return PaymentType;
	 }

	 public void setPaymentType(String paymentType) {
		 PaymentType = paymentType;
	 }

	
	 
	 
}
