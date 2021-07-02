package edu.utn.udee.Udee.config;

import edu.utn.udee.Udee.filter.JWTAuthorizationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.POST, "/api/receiver").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/api/backoffice/**").hasRole("BACKOFFICE")
                .antMatchers("/api/client/**").hasRole("CLIENT")
                //.antMatchers("/api/receiver").hasRole("RECEIVER")
                .anyRequest().authenticated()
                .and().httpBasic();

        http.headers().frameOptions().disable();
    }
}
