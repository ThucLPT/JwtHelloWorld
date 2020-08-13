package pkg.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import pkg.service.JwtService;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {
	@Autowired
	private JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		String token = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);
		jwtService.validate(token);
		filterChain.doFilter(request, response);
	}
}
