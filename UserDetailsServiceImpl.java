package in.main.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import in.main.Model.UserDetail;
import in.main.Repository.UserRepository;




@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    @Autowired
	private UserRepository urepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
		UserDetail user=urepo.findByemail(username);
		if(user == null) {
			throw new UsernameNotFoundException("user not found");
		}
		return new CustomUser(user);
	}

	
}
