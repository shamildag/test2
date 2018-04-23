package com.demo.springtask.config;

import org.apache.catalina.filters.RequestDumperFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.Filter;
import java.util.Arrays;

/**
 * Created by nydiarra on 06/05/17.
 */
@Configuration
public class AdditionalWebConfig {
	public static final Logger log = LoggerFactory.getLogger(AdditionalWebConfig.class);
	@Autowired
	private
	AutowireCapableBeanFactory beanFactory;
	/**
     * Allowing all origins, headers and methods here is only intended to keep this example simple.
     * This is not a default recommended configuration.
     *
     */
    @Bean
    public FilterRegistrationBean corsFilter() {
    	log.info ("corsFilter ");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }
    @Bean
    public FilterRegistrationBean requestDumperFilter() {
    	log.info("dumper filter ");
        FilterRegistrationBean registration = new FilterRegistrationBean();
        Filter requestDumperFilter = new RequestDumperFilter();
        registration.setFilter(requestDumperFilter);
        registration.addUrlPatterns("/*");
        return registration;
    }
	@Bean
	public FilterRegistrationBean daCors () {
		log.info("da cors  filter ");
		FilterRegistrationBean registration = new FilterRegistrationBean();
		Filter cors  = new DACorsFilter();
		beanFactory.autowireBean(cors);
		registration.setFilter(cors);
		registration.addUrlPatterns("/*");
		return registration;
	}
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
    	log.info("cors configuration ");
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList(""));
		configuration.setAllowedMethods(Arrays.asList(""));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
