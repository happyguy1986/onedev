package io.onedev.server.web.page;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import io.onedev.server.web.page.layout.LayoutPage;
import io.onedev.server.web.page.project.ProjectListPage;

@SuppressWarnings("serial")
public class DashboardPage extends LayoutPage {

	public DashboardPage(PageParameters params) {
		super(params);
		
		throw new RestartResponseException(ProjectListPage.class, ProjectListPage.paramsOf(0, 0));
	}

}
