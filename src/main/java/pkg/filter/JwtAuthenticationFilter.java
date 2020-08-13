package pkg.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import pkg.service.JwtService;

@Component
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	@Autowired
	private JwtService jwtService;

	@Autowired
	@Override
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		super.setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		try {
			JsonNode node = new ObjectMapper().readTree(request.getInputStream());
			Authentication authentication = new UsernamePasswordAuthenticationToken(
					node.get(SPRING_SECURITY_FORM_USERNAME_KEY).asText(), node.get(SPRING_SECURITY_FORM_PASSWORD_KEY).asText());
			return getAuthenticationManager().authenticate(authentication);
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
		response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtService.generate(authResult));
	}
}
