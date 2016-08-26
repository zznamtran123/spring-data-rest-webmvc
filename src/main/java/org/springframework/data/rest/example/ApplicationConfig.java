package org.springframework.data.rest.example;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = MyRepositoryImpl.class)
public class ApplicationConfig extends SpringBootServletInitializer {
	@Autowired
	private PersonRepository people;
	@Autowired
	private AddressRepository addresses;
	@Autowired
	private ProfileRepository profiles;

	public static void main(String[] args) {
		SpringApplication.run(ApplicationConfig.class, args);
	}

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
