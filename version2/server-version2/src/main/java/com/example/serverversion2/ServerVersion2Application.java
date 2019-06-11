package com.example.serverversion2;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.notify.AbstractEventNotifier;
import de.codecentric.boot.admin.server.notify.LoggingNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import reactor.core.publisher.Mono;

//@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@EnableAdminServer
public class ServerVersion2Application {

    public static void main(String[] args) {
        SpringApplication.run(ServerVersion2Application.class, args);
    }

    @Configuration
    public class SecuritySecureConfig extends WebSecurityConfigurerAdapter {

        private final String adminContextPath;

        public SecuritySecureConfig(AdminServerProperties adminServerProperties) {
            this.adminContextPath = adminServerProperties.getContextPath();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
            successHandler.setTargetUrlParameter("redirectTo");
            successHandler.setDefaultTargetUrl(adminContextPath + "/");

            http.authorizeRequests()
                    .antMatchers(adminContextPath + "/assets/**").permitAll()
                    .antMatchers(adminContextPath + "/login").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin().loginPage(adminContextPath + "/login").successHandler(successHandler).and()
                    .logout().logoutUrl(adminContextPath + "/logout").and()
                    .httpBasic().and()
                    .csrf()
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .ignoringAntMatchers(
                            adminContextPath + "/instances",
                            adminContextPath + "/actuator/**",
                            adminContextPath + "/**"
                    );
        }
    }

    @Configuration
    public class CustomNotifier extends AbstractEventNotifier {

        private final Logger LOGGER = LoggerFactory.getLogger(LoggingNotifier.class);

        public CustomNotifier(InstanceRepository repository) {
            super(repository);
        }

        @Override
        protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
            return Mono.fromRunnable(() -> {
                if (event instanceof InstanceStatusChangedEvent) {
                    LOGGER.info("Instance {} ({}) is {}", instance.getRegistration().getName(), event.getInstance(),
                            ((InstanceStatusChangedEvent) event).getStatusInfo().getStatus());
                } else {
                    LOGGER.info("Instance {} ({}) {}", instance.getRegistration().getName(), event.getInstance(),
                            event.getType());
                }
            });
        }
    }
}
