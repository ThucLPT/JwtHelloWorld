package pkg.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.*;

@Service
public class JwtService {
	public String generate(Authentication authentication) {
		int time = 5 * 60 * 1000;
		String token = Jwts.builder()
				.setSubject(authentication.getName())
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + time))
				.claim("authorities", authentication.getAuthorities())
				.signWith(SignatureAlgorithm.HS256, new byte[32])
				.compact();
		return token;
	}

	@SuppressWarnings("unchecked")
	public void validate(String token) {
		Claims claims = Jwts.parser().setSigningKey(new byte[32]).parseClaimsJws(token).getBody();
		List<GrantedAuthority> authorities = ((List<Map<String, String>>) claims.get("authorities")).stream()
				.map(m -> new SimpleGrantedAuthority(m.get("authority"))).collect(Collectors.toList());
		Authentication authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
