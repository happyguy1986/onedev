package io.onedev.server.search.entity.project;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import io.onedev.commons.utils.match.WildcardUtils;
import io.onedev.server.model.Project;
import io.onedev.server.model.User;
import io.onedev.server.search.entity.EntityCriteria;
import io.onedev.server.util.query.ProjectQueryConstants;

public class ForksOfCriteria extends EntityCriteria<Project> {

	private static final long serialVersionUID = 1L;

	private final String projectName;
	
	public ForksOfCriteria(String projectName) {
		this.projectName = projectName;
	}

	@Override
	public Predicate getPredicate(Root<Project> root, CriteriaBuilder builder, User user) {
		Path<String> attribute = root
				.join(ProjectQueryConstants.ATTR_FORKED_FROM, JoinType.INNER)
				.get(ProjectQueryConstants.ATTR_NAME);
		String normalized = projectName.toLowerCase().replace("*", "%");
		return builder.like(builder.lower(attribute), normalized);
	}

	@Override
	public boolean matches(Project project, User user) {
		Project forkedFrom = project.getForkedFrom();
		if (forkedFrom != null) {
			return WildcardUtils.matchString(projectName.toLowerCase(), 
					forkedFrom.getName().toLowerCase());
		} else {
			return false;
		}
	}

	@Override
	public boolean needsLogin() {
		return false;
	}

	@Override
	public String toString() {
		return ProjectQuery.getRuleName(ProjectQueryLexer.ForksOf) + " " + ProjectQuery.quote(projectName);
	}

}
