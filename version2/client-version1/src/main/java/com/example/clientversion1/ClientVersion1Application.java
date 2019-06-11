package com.example.clientversion1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientVersion1Application {

    public static void main(String[] args) {
        SpringApplication.run(ClientVersion1Application.class, args);
    }

//    @Configuration
//    public static class SecurityPermitAllConfig extends WebSecurityConfigurerAdapter {
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http.authorizeRequests().antMatchers("/**").hasIpAddress("127.0.0.1").anyRequest().authenticated()
//                    .and().csrf().disable();
//
////            http.authorizeRequests().antMatchers("/actuator/**").permitAll().anyRequest().authenticated()
////                    .and().csrf().disable();
//        }
//    }
}
