package in.main.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import in.main.Model.UserDetail;

public interface UserRepository extends JpaRepository<UserDetail, Integer>{
    
	
	public UserDetail findByemail(String email);

	public List<UserDetail> findByRole(String role);
	
	 public UserDetail findByResetToken(String token);
	
	public Boolean existsByEmail(String email);
}
