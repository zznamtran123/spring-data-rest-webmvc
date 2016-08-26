package org.springframework.data.rest.example;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@SpringBootApplication
public class ApplicationConfig extends SpringBootServletInitializer {
	@Autowired
	private PersonRepository people;
	@Autowired
	private AddressRepository addresses;
	@Autowired
	private ProfileRepository profiles;

	//@javax.annotation.PostConstruct
	void load() {
		people.save(new Person("John Doe",
				Arrays.asList(addresses.save(new Address(Arrays.asList("123 W. 1st St."), "Univille", "US", "12345"))),
				Arrays.asList(profiles.save(new Profile("twitter", "http://twitter.com/john_doe")))));
	}

	@Bean
	public FilterRegistrationBean myFilter2() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		CommonsRequestLoggingFilter requestDumperFilter = new CommonsRequestLoggingFilter();
		requestDumperFilter.setIncludePayload(true);
		requestDumperFilter.setMaxPayloadLength(1000);
		//requestDumperFilter.setIncludeClientInfo(true);
		//requestDumperFilter.setIncludeHeaders(true);
		requestDumperFilter.setIncludeQueryString(true);
		registration.setFilter(requestDumperFilter);
		registration.addUrlPatterns("/*");
		return registration;
	}
}
