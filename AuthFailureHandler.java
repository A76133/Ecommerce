package in.main.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import in.main.Model.UserDetail;
import in.main.Repository.UserRepository;
import in.main.Service.userService;
import in.main.util.AppContent;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	@Autowired
	private UserRepository Urepo;
	@Autowired
	private userService Userv;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		String email=request.getParameter("username");
    UserDetail userDtls=Urepo.findByemail(email);
      
    if(userDtls !=null) {
		
		if(userDtls.getIsEnable()) {
			if(userDtls.getAccountNonLocked()) {
				if(userDtls.getFailedAttempt()<=AppContent.ATTEMPT_TIME) {
					Userv.increaseFailedAttemt(userDtls);
				}else {
					Userv.userAccountLock(userDtls);
					exception=new LockedException("your account is Locked || failed Attempt");
				}
			}else {
				if(Userv.unlockedAccountTimeExpired(userDtls)) {
					exception=new LockedException("your account is UnLocked || please try to login");
				}else {
					exception=new LockedException("your account is Locked || please try after SomeTimes");
				}
				
			}
		}else {
			exception=new LockedException("your account is inactive");
		}}else {
			exception=new LockedException("email and password is invalid");
		}
		super.setDefaultFailureUrl("/signin?error");
		super.onAuthenticationFailure(request, response, exception);
	}

	@Override
	public void setDefaultFailureUrl(String defaultFailureUrl) {
		// TODO Auto-generated method stub
		super.setDefaultFailureUrl(defaultFailureUrl);
	}

	@Override
	protected boolean isUseForward() {
		// TODO Auto-generated method stub
		return super.isUseForward();
	}

	@Override
	public void setUseForward(boolean forwardToDestination) {
		// TODO Auto-generated method stub
		super.setUseForward(forwardToDestination);
	}

	@Override
	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		// TODO Auto-generated method stub
		super.setRedirectStrategy(redirectStrategy);
	}

	@Override
	protected RedirectStrategy getRedirectStrategy() {
		// TODO Auto-generated method stub
		return super.getRedirectStrategy();
	}

	@Override
	protected boolean isAllowSessionCreation() {
		// TODO Auto-generated method stub
		return super.isAllowSessionCreation();
	}

	@Override
	public void setAllowSessionCreation(boolean allowSessionCreation) {
		// TODO Auto-generated method stub
		super.setAllowSessionCreation(allowSessionCreation);
	}

}
