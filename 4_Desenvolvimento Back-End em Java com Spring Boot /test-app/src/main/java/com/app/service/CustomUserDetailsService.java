package com.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.jdbc.core.JdbcTemplate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

	private final JdbcTemplate jdbcTemplate;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<Map<String, Object>> users = jdbcTemplate.queryForList(
				"SELECT full_name, email, password_hash, role, enabled FROM app_users WHERE email = ?",
				username);

		if (users.isEmpty()) {
			throw new UsernameNotFoundException("User not found: " + username);
		}

		Map<String, Object> row = users.get(0);
		String email = (String) row.get("email");
		String passwordHash = (String) row.get("password_hash");
		String role = (String) row.get("role");
		Boolean enabled = (Boolean) row.get("enabled");

		return User.withUsername(email)
				.password(passwordHash)
				.authorities(List.of(new SimpleGrantedAuthority(role)))
				.disabled(!Boolean.TRUE.equals(enabled))
				.build();
	}
}