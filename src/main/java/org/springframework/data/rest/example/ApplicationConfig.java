package org.springframework.data.rest.example;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class ApplicationConfig extends SpringBootServletInitializer {
	@Autowired
	private PersonRepository people;
	@Autowired
	private AddressRepository addresses;
	@Autowired
	private ProfileRepository profiles;

	@PostConstruct
	private void load() {
		Map<String, Profile> profs = new HashMap<String, Profile>();
		profs.put("twitter", profiles.save(new Profile("twitter", "http://twitter.com/john_doe")));
		people.save(new Person("John Doe",
				Arrays.asList(addresses.save(new Address(Arrays.asList("123 W. 1st St."), "Univille", "US", "12345"))),
				profs));
	}
}
