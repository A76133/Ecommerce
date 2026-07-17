package in.main.Service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import in.main.Model.UserDetail;
import in.main.Repository.UserRepository;
import in.main.Service.userService;
import in.main.util.AppContent;
@Service
public class userServiceImpl implements userService {
    @Autowired
	private UserRepository Urepo;
    
    @Autowired
    private PasswordEncoder passwordEncoder;   // 🔥 ADD THIS

    
	
	
	 @Override
	    public UserDetail saveuser(UserDetail user) {

	        // 🔥 MOST IMPORTANT LINE
		     user.setRole("ROLE_USER");
		     user.setIsEnable(true);
		     user.setAccountNonLocked(true);
		     user.setFailedAttempt(0);
		     
	        user.setPassword(passwordEncoder.encode(user.getPassword()));

	        return Urepo.save(user);
	    }
	 
	 @Override
		public UserDetail saveAdmin(UserDetail user) {
		 // 🔥 MOST IMPORTANT LINE
	     user.setRole("ROLE_ADMIN");
	     user.setIsEnable(true);
	     user.setAccountNonLocked(true);
	     user.setFailedAttempt(0);
	     
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return Urepo.save(user);
		}
	 
	
		

	@Override
	public UserDetail getuserbyemail(String email) {
		// TODO Auto-generated method stub
		return Urepo.findByemail(email);
	}

	@Override
	public List<UserDetail> getusers(String role) {
		// TODO Auto-generated method stub
		return Urepo.findByRole(role);
	}

	@Override
	public Boolean updateAccountStatus(Integer id, Boolean status) {
		// TODO Auto-generated method stub
		
		Optional<UserDetail> byId = Urepo.findById(id);
		
		if(byId.isPresent()) {
			
			UserDetail userDetail = byId.get();
			userDetail.setIsEnable(status);
			Urepo.save(userDetail);
			return true;
		}
		return false;
	}

	@Override
	public void increaseFailedAttemt(UserDetail user) {
		int attempt=user.getFailedAttempt()+1;
		user.setFailedAttempt(attempt);
		Urepo.save(user);
		
	}

	@Override
	public void userAccountLock(UserDetail user) {
		user.setAccountNonLocked(false);
		user.setLocktime(new Date());
	    Urepo.save(user);	
		
	}

	@Override
	public Boolean unlockedAccountTimeExpired(UserDetail user) {
		
		long lockTime=user.getLocktime().getTime();
		long unlocktime=lockTime+AppContent.UNLOCK_DURATION_TIME;
		
	    Long currentTime=System.currentTimeMillis();
	    
	    if(unlocktime<currentTime) {
	    	user.setAccountNonLocked(true);
	    	user.setFailedAttempt(0);
	    	user.setLocktime(null);
	    	Urepo.save(user);
	    	return true;
	    	
	    }
		
		return false;
	}

	@Override
	public void resetAttempt(int userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateuserresettoken(String email, String resettoken) {
		UserDetail findemail=Urepo.findByemail(email);
		findemail.setResetToken(resettoken);
		Urepo.save(findemail);
		
	}
	
	

	@Override
	public UserDetail getuserbytoken(String token) {
		return Urepo.findByResetToken(token);
		
	}

	@Override
	public UserDetail updateuser(UserDetail user) {
		return Urepo.save(user);
		
	}

	@Override
	public UserDetail updateuserprofile(UserDetail user, MultipartFile img) {

	    UserDetail dbuser = Urepo.findById(user.getId()).get();

	    // normal fields update
	    dbuser.setName(user.getName());
	    dbuser.setMobileNumber(user.getMobileNumber());
	    dbuser.setAddress(user.getAddress());
	    dbuser.setCity(user.getCity());
	    dbuser.setState(user.getState());
	    dbuser.setPincode(user.getPincode());

	    try {

	        // image update only if new image selected
	        if (!img.isEmpty()) {

	            dbuser.setProfile_image(img.getOriginalFilename());

	            File saveDir = new ClassPathResource("static/Img/Saved").getFile();

	            Path path = Paths.get(
	                    saveDir.getAbsolutePath()
	                    + File.separator
	                    + img.getOriginalFilename());

	            Files.copy(
	                    img.getInputStream(),
	                    path,
	                    StandardCopyOption.REPLACE_EXISTING
	            );
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return Urepo.save(dbuser);
	}

	@Override
	public Boolean existsEmail(String emial) {
		return Urepo.existsByEmail(emial);
		 
	}

	
}
