package in.main.Service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import in.main.Model.UserDetail;

public interface userService {
 
	public UserDetail saveuser(UserDetail user);
	
	public UserDetail saveAdmin(UserDetail user);
	
	
	public UserDetail getuserbyemail(String email);
	
	public List<UserDetail> getusers(String role);

	public Boolean updateAccountStatus(Integer id, Boolean status);
	
	public void increaseFailedAttemt(UserDetail user);
	
	public void userAccountLock(UserDetail user);
	
	public Boolean unlockedAccountTimeExpired(UserDetail user);
	
	public void resetAttempt(int userId);

	public void updateuserresettoken(String email, String resettoken);
	
	public UserDetail getuserbytoken(String token);
	
	public UserDetail updateuser(UserDetail user);
	
	public UserDetail updateuserprofile(UserDetail user,MultipartFile img);
	
	public Boolean existsEmail(String emial);
}
