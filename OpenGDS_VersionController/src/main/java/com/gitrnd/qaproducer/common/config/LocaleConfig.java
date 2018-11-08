package com.gitrnd.qaproducer.common.config;

import java.util.Locale;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class LocaleConfig extends WebMvcConfigurerAdapter {
	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		// WEB-INF 밑에 해당 폴더에서 properties를 찾는다.
		// messageSource.setBasename("/WEB-INF/jsp/");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		// request로 넘어오는 language parameter를 받아서 locale로 설정 한다.
		localeChangeInterceptor.setParamName("lang");
		return localeChangeInterceptor;
	}

	@Bean(name = "localeResolver")
	public LocaleResolver sessionLocaleResolver() {
		// 세션 기준으로 로케일을 설정 한다.
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		// 쿠키 기준(세션이 끊겨도 브라우져에 설정된 쿠키 기준으로)
		// CookieLocaleResolver localeResolver = new CookieLocaleResolver();

		// 최초 기본 로케일을 강제로 설정이 가능 하다.
//		localeResolver.setDefaultLocale(new Locale("en_US"));
		return localeResolver;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// Interceptor를 추가 한다.
		registry.addInterceptor(localeChangeInterceptor());
	}

}
