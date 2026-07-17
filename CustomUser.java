//Spring security flow
//                         Authentcation
//                               |
//                             2 |
//                 1             |           3                       4
//user entered ----------->Authontication --------->Authentication-------->Authentication 
//credentials  <---------    filter      <--------     manager      <------    provider
//                   10         |             8                       7       |         |
//                             9|                                            5|        6|
//                              |                                             |         |
//                            Secutiry                                 User Details   password
//                            Context                                    service       encoder



//Steps of spring security
//
//1. Add Spring security dependency
//2. Add role is user model
//3. create custom user and implement userdetals  set role,email,password
//4. Create userdetailsserviceimpl and implement userdetailservice
//5. create securityconfig
//6. create authsuccesshandler



package in.main.config;

import java.util.Arrays;
import java.util.Collection;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import in.main.Model.UserDetail;

public class CustomUser implements UserDetails {
     
	private UserDetail user;
	
	
	
	public CustomUser(UserDetail user) {
		super();
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority authority=new SimpleGrantedAuthority(user.getRole());
		return Arrays.asList(authority);
	}

	@Override
	public @Nullable String getPassword() {
		
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getEmail();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return user.getIsEnable();
	}

  
	
}
