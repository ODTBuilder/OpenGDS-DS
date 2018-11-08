package com.gitrnd.qaproducer.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.gitrnd.qaproducer.common.handler.UserLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsService userDetailsService;

	@Autowired
	UserLogoutSuccessHandler userLogoutSuccessHandler;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/map.do").authenticated().antMatchers("/validation.do").authenticated()
				.antMatchers("/list.do").authenticated().antMatchers("/setting.do").authenticated()
				.antMatchers("/createpreset.do").authenticated().antMatchers("/settinglist.do").authenticated()
				.antMatchers("/option/**").authenticated().antMatchers("/downloaderror.do").authenticated()
				.antMatchers("/geoserver/**").authenticated().antMatchers("/file/**").authenticated()
				.antMatchers("/upload.do").authenticated().antMatchers("/user/userinfo.do").authenticated()
				.antMatchers("/user/deactivateuser.ajax").authenticated();
		http.formLogin().loginPage("/user/signin.do").permitAll().loginProcessingUrl("/user/signinProcess.do")
				.defaultSuccessUrl("/map.do").failureUrl("/user/signin.do");
		http.logout().logoutRequestMatcher(new AntPathRequestMatcher("/user/signout.do"))
				.logoutSuccessHandler(userLogoutSuccessHandler);
		// .logoutSuccessUrl("/main.do")
		http.csrf().ignoringAntMatchers("/uploaderror.do").and().csrf().ignoringAntMatchers("/mobile/validate.do");
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
}
