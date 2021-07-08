package com.ns.chemcomp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetails;

    /**
     * Ensure passwords are coded before written to db
     *
     * @param auth {AuthenticationManagerBuilder} to create authentication
     * @throws Exception if errors on adding authentication
     */
    @Autowired
    public void configureGlobalInDB(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetails).passwordEncoder(bCryptPasswordEncoder());
    }

    /**
     * Initialize password encoder
     *
     * @return {BCryptPasswordEncoder} for use as a spring bean
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configure web security setup for domain
     *
     * @param http {HttpSecurity}
     * @throws Exception if any exception is thrown from web security
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //FIXME fill in rest of pages
        http.authorizeRequests()
                .antMatchers("/admin").hasRole("ADMIN") //admin control panel
                .antMatchers("/", "/home", "/login", "/products", "/categories", "/viewCategory", "/viewProduct").permitAll()
                .antMatchers("/styles/**", "/js/**", "/fonts/**", "/images/**").permitAll()
                .and()
                .formLogin().loginPage("/login").failureUrl("/login?login_error=1") //if login fails, go to this url.permitAll()
                .and().logout().logoutSuccessUrl("/").permitAll();//on log out, go to base page
    }
}
