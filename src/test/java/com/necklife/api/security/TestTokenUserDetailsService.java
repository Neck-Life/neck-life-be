package com.necklife.api.security;

import com.necklife.api.security.authentication.authority.Roles;
import com.necklife.api.security.authentication.token.TokenUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class TestTokenUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return TokenUserDetails.builder()
				.id("1")
				.authorities(List.of(Roles.ROLE_USER.getAuthority()))
				.build();
	}
}
