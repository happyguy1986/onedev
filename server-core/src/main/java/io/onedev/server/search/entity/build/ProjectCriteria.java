package io.onedev.server.search.entity.build;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import io.onedev.commons.utils.match.WildcardUtils;
import io.onedev.server.model.Build;
import io.onedev.server.model.User;
import io.onedev.server.search.entity.EntityCriteria;
import io.onedev.server.util.query.BuildQueryConstants;
import io.onedev.server.util.query.ProjectQueryConstants;

public class ProjectCriteria extends EntityCriteria<Build> {

	private static final long serialVersionUID = 1L;
	
	private final String projectName;

	public ProjectCriteria(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public Predicate getPredicate(Root<Build> root, CriteriaBuilder builder, User user) {
		Path<String> attribute = root
				.join(BuildQueryConstants.ATTR_PROJECT, JoinType.INNER)
				.get(ProjectQueryConstants.ATTR_NAME);
		String normalized = projectName.toLowerCase().replace("*", "%");
		return builder.like(builder.lower(attribute), normalized);
	}

	@Override
	public boolean matches(Build build, User user) {
		return WildcardUtils.matchString(projectName.toLowerCase(), 
				build.getProject().getName().toLowerCase());
	}

	@Override
	public boolean needsLogin() {
		return false;
	}

	@Override
	public String toString() {
		return BuildQuery.quote(BuildQueryConstants.FIELD_PROJECT) + " " 
				+ BuildQuery.getRuleName(BuildQueryLexer.Is) + " " 
				+ BuildQuery.quote(projectName);
	}

}
