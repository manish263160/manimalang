package com.manimalang.security;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.manimalang.Enums.STATUS;
import com.manimalang.models.User;
import com.manimalang.service.UserService;



@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

	private static final Logger logger = Logger.getLogger(CustomUserDetailsService.class);
	@Autowired
	UserService userService;

	
	
	@Autowired
	public CustomUserDetailsService(UserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
		
		User user = userService.checkUserByEmailorID(emailId);
		if (user == null) {
			throw new UsernameNotFoundException(String.format("User %s does not exist!", emailId));
		}
		
			List<String> roles = null;
			roles = userService.getUserRoles(user.getUserId());
			
			 if (roles == null || roles.isEmpty()) {
		            throw new UsernameNotFoundException("User not authorized.");
		        }
			Collection<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
			 if (roles != null) {
					for (String role : roles) {
						// ROLE_USER, ROLE_ADMIN,..
						GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
						grantList.add(authority);
					}
				}
		
		 return new UserRepositoryUserDetails(user,userService);
	}

	private final static class UserRepositoryUserDetails extends User implements UserDetails {

		private static final long serialVersionUID = 1L;
		private User user;
		private UserService userService;
		

		private UserRepositoryUserDetails(User user,UserService userService ) {
			this.user=user;
			this.userService = userService;
			
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			try {
			List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
			if (user != null && STATUS.INACTIVE.ID == user.getStatus()) {
				throw new UsernameNotFoundException(String.format(
						URLEncoder.encode("You are not active", "UTF-8"), user.getMobileNo()));
			}

			if (user != null && STATUS.BLOCK.ID == user.getStatus()) {
				throw new UsernameNotFoundException(
						String.format(URLEncoder.encode("You are blocked. Please contact admin", "UTF-8"),
								user.getMobileNo()));
			}
			List<String> roles = null;
			if (user != null) {
				roles = userService.getUserRoles(user.getUserId());
			}
			if (roles != null) {
				for (String role : roles) {
					// ROLE_USER, ROLE_ADMIN,..
					GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
					grantList.add(authority);
				}
			}
			return grantList;
			}catch (Exception e) {
				logger.error("Error===", e);
				throw new AuthenticationServiceException(e.getMessage());
			}
			
		}

		@Override
        public String getUsername() {
            return getName();
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
            return true;
        }

	}

}
