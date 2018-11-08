package com.gitrnd.qaproducer.common.config;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class ErrorPageConfig extends ServerProperties {
	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		super.customize(container);

		container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/error/404"));
		container.addErrorPages(new ErrorPage(HttpStatus.FORBIDDEN, "/error/403"));
		container.addErrorPages(new ErrorPage(HttpStatus.UNAUTHORIZED, "/error/401"));
	}
}
