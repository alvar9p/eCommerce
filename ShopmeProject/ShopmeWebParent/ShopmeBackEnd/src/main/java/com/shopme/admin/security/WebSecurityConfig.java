package com.shopme.admin.security;

import com.shopme.admin.user.service.ShopmeUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// La anotacion Configuration indica que es una clase de conf de Spring
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService(){
        return new ShopmeUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        // Se sobreescribe este metodo para permitir entrar sin autenticarse
        // rememberMe() para guardar al usuario logeado (cerrar navegador)
        // 7 dias * 24 horas * 60 minutos (validity)
        httpSecurity.authorizeRequests()
                .antMatchers("/users/**").hasAuthority("Admin")
                .antMatchers("/categories/**", "/brands/**").hasAnyAuthority("Admin", "Editor")
                .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .usernameParameter("email")
                    .permitAll()
                .and()
                    .logout()
                    .permitAll()
                .and()
                    .rememberMe()
                    .key("AbcDefgHijKlmnOprqs_1234567890")
                    .tokenValiditySeconds(7 * 24 * 60 * 60);
    }

    // Carpetas que se ignoran para ver la pagina del login
    @Override
    public void configure(WebSecurity web) throws Exception{
        web.ignoring().antMatchers("/images/**", "js/", "/webjars/**");
    }
}
