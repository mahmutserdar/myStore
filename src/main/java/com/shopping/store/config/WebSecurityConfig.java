package com.shopping.store.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password(passwordEncoder().encode("user")).roles("USER")
                .and()
                .withUser("admin").password(passwordEncoder().encode("admin")).roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/home", "/items").permitAll()
                .antMatchers("/items/add", "/items/edit/**", "/items/update/**", "/items/delete/**").hasRole("ADMIN")
                .antMatchers("/h2-console/**", "/debugSession", "/items/search/**","/items/basket/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(new CustomAuthenticationSuccessHandler())
                .permitAll()
                .and()
                .logout()
                .logoutSuccessHandler(new CustomLogoutSuccessHandler("/items"))
                .permitAll()
                .and()
                .csrf()
                .ignoringAntMatchers("/h2-console/**") // Disable CSRF protection for the H2 console
                .and()
                .headers()
                .frameOptions()
                .sameOrigin();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

