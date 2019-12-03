package io.onedev.server.search.entity.build;

import javax.annotation.Nullable;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import io.onedev.server.model.Build;
import io.onedev.server.model.BuildDependence;
import io.onedev.server.model.Project;
import io.onedev.server.model.User;
import io.onedev.server.search.entity.EntityCriteria;
import io.onedev.server.search.entity.EntityQuery;
import io.onedev.server.util.query.BuildQueryConstants;

public class DependsOnCriteria extends EntityCriteria<Build> {

	private static final long serialVersionUID = 1L;

	private final Build build;
	
	private final String value;
	
	public DependsOnCriteria(@Nullable Project project, String value) {
		build = EntityQuery.getBuild(project, value);
		this.value = value;
	}

	@Override
	public Predicate getPredicate(Root<Build> root, CriteriaBuilder builder, User user) {
		From<?, ?> join = root.join(BuildQueryConstants.ATTR_DEPENDENCIES, JoinType.LEFT);
		return builder.equal(join.get(BuildDependence.ATTR_DEPENDENCY), build);
	}

	@Override
	public boolean matches(Build build, User user) {
		for (BuildDependence dependence: this.build.getDependents()) {
			if (dependence.getDependent().equals(build))
				return true;
		}
		return false;
	}

	@Override
	public boolean needsLogin() {
		return false;
	}

	@Override
	public String toString() {
		return BuildQuery.getRuleName(BuildQueryLexer.DependsOn) + " " + BuildQuery.quote(value);
	}

}
