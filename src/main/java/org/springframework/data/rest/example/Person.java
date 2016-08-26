package org.springframework.data.rest.example;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;

@Entity
public class Person {

	@Id
	@GeneratedValue
	private Long id;
	private String name;
	@Version
	private Long version;
	@OneToMany
	private List<Address> addresses;
	@OneToMany
	private List<Profile> profiles;

	public Person() {
	}

	public Person(String name, List<Address> addresses, List<Profile> userProfiles) {
		this.name = name;
		this.addresses = addresses;
		this.profiles = userProfiles;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	public List<Profile> getProfiles() {
		return profiles;
	}

	public void setProfiles(List<Profile> profiles) {
		this.profiles = profiles;
	}
}