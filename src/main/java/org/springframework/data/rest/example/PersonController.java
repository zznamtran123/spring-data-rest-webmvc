package org.springframework.data.rest.example;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.RepositorySearchesResource;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RepositoryRestController
@ExposesResourceFor(Person.class)
public class PersonController implements ResourceProcessor<RepositorySearchesResource> {

	private final PersonRepository repository;

	@Autowired
	private EntityLinks entityLinks;

	@Autowired
	PagedResourcesAssembler assembler;
	
	@Autowired
	public PersonController(PersonRepository repo) {
		repository = repo;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/persons/search/rsql")
	public @ResponseBody PagedResources<?> search(HttpServletRequest req, PersistentEntityResourceAssembler eass) {
		Page<Person> entities = repository.findByRsql(req.getQueryString());
		return assembler.toResource(entities, eass);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/persons/search/findByRsql")
	public @ResponseBody PagedResources<?> findRsql(@RequestParam(value = "rsql") String rsql, Pageable pageable,
			PersistentEntityResourceAssembler eass) {
		Page<Person> entities = repository.findByRsql(rsql, pageable);
		return assembler.toResource(entities, eass);
	}

	@Override
	public RepositorySearchesResource process(RepositorySearchesResource resource) {
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(getClass()).findRsql("",null,null))
				.withRel("findByRsql"));
		resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(getClass()).search(null,null))
				.withRel("rsql"));
		return resource;
	}

}
