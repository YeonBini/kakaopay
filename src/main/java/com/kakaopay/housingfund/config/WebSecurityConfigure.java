package com.kakaopay.housingfund.config;

import com.kakaopay.housingfund.security.EntryPointUnauthorizedHandler;
import com.kakaopay.housingfund.security.JwtAccessDeniedHandler;
import com.kakaopay.housingfund.security.JwtAuthenticationFilter;
import com.kakaopay.housingfund.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private final EntryPointUnauthorizedHandler entryPointUnauthorizedHandler;

    public WebSecurityConfigure(JwtTokenProvider jwtTokenProvider, JwtAccessDeniedHandler jwtAccessDeniedHandler, EntryPointUnauthorizedHandler entryPointUnauthorizedHandler) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.entryPointUnauthorizedHandler = entryPointUnauthorizedHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin().disable()
                .headers().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                    .accessDeniedHandler(jwtAccessDeniedHandler)
                    .authenticationEntryPoint(entryPointUnauthorizedHandler)
                .and()
                .authorizeRequests()
                    .antMatchers("/institute/list").permitAll()
                    .antMatchers("/institute/housing-fund/list").permitAll()
                    .antMatchers("/institute/housing-fund/*/max").permitAll()
                    .antMatchers("/institute/housing-fund/save").hasRole("USER")
                    .antMatchers("/user/signin", "/user/signup").permitAll()
                    .antMatchers("/h2-console/**").permitAll()
                    .anyRequest().hasRole("USER")
                .and()
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/h2-console/**","/swagger-resources/**",
                "/swagger-ui.html", "/webjars/**", "/swagger/**");
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
