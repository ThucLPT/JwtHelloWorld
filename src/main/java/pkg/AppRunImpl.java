package pkg;

import org.springframework.boot.CommandLineRunner;
import org.springframework.http.*;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

//@Component
public class AppRunImpl implements CommandLineRunner {
	@Override
	public void run(String... args) throws Exception {
		RestTemplate template = new RestTemplate();

		UserDetails userDetails = new User("user", "pass", AuthorityUtils.NO_AUTHORITIES);
		HttpEntity<?> entity = new HttpEntity<>(userDetails);
		ResponseEntity<?> response = template.exchange("http://localhost:8080/login", HttpMethod.POST, entity, Object.class);
		String token = response.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
		System.out.println(token);

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, token);
		HttpEntity<?> httpEntity = new HttpEntity<>(headers);
		ResponseEntity<?> responseEntity = template.exchange("http://localhost:8080", HttpMethod.GET, httpEntity, String.class);
		System.out.println(responseEntity.getBody());
	}
}
