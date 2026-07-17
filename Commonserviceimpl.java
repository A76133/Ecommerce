package in.main.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
@Service
public class Commonserviceimpl implements commonservice{

	@Override
	public void removesessionmsg(){
		
		HttpServletRequest request=((ServletRequestAttributes)(RequestContextHolder.getRequestAttributes())).getRequest();
	    HttpSession session=request.getSession();
	    session.removeAttribute("successMsg");
	    session.removeAttribute("errorMsg");
	}

}
