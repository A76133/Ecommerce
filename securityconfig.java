package in.main.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class securityconfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    
  
    
    @Autowired
    private AuthenticationSuccessHandler auth;
    
    @Autowired
    private AuthFailureHandler Auth;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationprovider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider(userDetailsService);
       
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    public SecurityFilterChain filterchain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable())
            .authenticationProvider(authenticationprovider())  
            .authorizeHttpRequests(req -> req
                .requestMatchers("/user/**").hasRole("USER")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/**").permitAll())
            .formLogin(form -> form
                .loginPage("/signin")
                .loginProcessingUrl("/login")
    //            .defaultSuccessUrl("/")
                .failureHandler(Auth)
                .successHandler(auth))
            
            .logout(logout -> logout.permitAll());

        return http.build();
    }
}