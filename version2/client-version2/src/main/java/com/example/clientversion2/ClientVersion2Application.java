package com.example.clientversion2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@SpringBootApplication
public class ClientVersion2Application {

    public static void main(String[] args) {
        SpringApplication.run(ClientVersion2Application.class, args);
    }

    @Configuration
    public static class SecurityPermitAllConfig extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
//            http.authorizeRequests().antMatchers("/actuator/**").hasIpAddress("127.0.0.1").anyRequest().authenticated()
//                    .and().csrf().disable();

            http.authorizeRequests().antMatchers("/actuator/**").permitAll().anyRequest().authenticated()
                    .and().csrf().disable();
        }
    }
}
