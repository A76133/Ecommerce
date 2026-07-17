package in.main.util;

import java.io.UnsupportedEncodingException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import in.main.Model.ProductOrder;
import in.main.Model.UserDetail;
import in.main.Service.userService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
@Component
public class CommonUtil {
	@Autowired
   private  JavaMailSender mail;
	
	
	public  Boolean sendmail(String url,String recieptemail) throws UnsupportedEncodingException, MessagingException {
		MimeMessage message=mail.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message);
		
		helper.setFrom("anjalilodhi772@gmail.com", "shoping_card");
		helper.setTo(recieptemail);
		
		String content="<p>Hellow</p> + <p>you have requested to reset your password </p>"
				+ "<p>Click the link below to change your password : </p>"
				+ "<p><a href=\""+ url + "\">change my password</a></p>";
		
		helper.setSubject("password reset");
		helper.setText(content,true);
		
		mail.send(message);
		
	    return true;	
	}

	public static String generateUrl(HttpServletRequest request) {
	    String siteUrl = request.getRequestURL().toString();
	    return siteUrl.replace(request.getServletPath(), "");
	}
	
	String msg=null;
	
	
	public Boolean SendMailForProductUser(ProductOrder order,String status) throws UnsupportedEncodingException, MessagingException {
		
		msg="<p>Helow [[name]],"
				+ "</p>"
				
		          +"<p>thanku order [[orderStatus]]</b>.</p>"
	              +"<p>Product Details : </p>"
				  +"<p>Name: [[ProductName]]</p>"
				  +"<p>Category : [[Category]]</p>"
				  +"<p>Quantity : [[Quantity]]</p>"
				  +"<p>Price : [[Price]]</p>"
				  +"<p>Payement Type : [[payementtype]]</p>";
		
		
		MimeMessage message=mail.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message);
		
		helper.setFrom("anjalilodhi772@gmail.com", "shoping_card");
		helper.setTo(order.getOrderAddress().getEmail());
		
		msg=msg.replace("[[name]]", order.getOrderAddress().getFirstName());
		msg=msg.replace("[[orderStatus]]", status);
		msg=msg.replace("[[ProductName]]", order.getProduct().getTitle());
		msg=msg.replace("[[Category]]", order.getProduct().getCategory());
		msg=msg.replace("[[Quantity]]", order.getQuantity().toString());
		msg=msg.replace("[[Price]]", order.getPrice().toString());
		msg=msg.replace("[[payementtype]]", order.getPaymentType());
		
		helper.setSubject("Product Order");
		helper.setText(msg,true);
		
		mail.send(message);
		
	    return true;	
		
	}
	
	
}
