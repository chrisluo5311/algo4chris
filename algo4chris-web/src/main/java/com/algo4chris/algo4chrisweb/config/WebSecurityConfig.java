package com.algo4chris.algo4chrisweb.config;

import com.algo4chris.algo4chrisweb.controller.logout.LogoutHandler;
import com.algo4chris.algo4chrisweb.security.jwt.AuthEntryPointJWT;
import com.algo4chris.algo4chrisweb.security.jwt.AuthTokenFilter;
import com.algo4chris.algo4chrisweb.security.jwt.CustomAccessDeniedHandler;
import com.algo4chris.algo4chrisweb.security.services.UserDetailsServiceImpl;
import com.algo4chris.algo4chrisweb.security.services.impl.AlgoAuthSuccessHandler;
import com.algo4chris.algo4chrisweb.security.services.impl.AlgoOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import javax.annotation.Resource;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    UserDetailsServiceImpl userDetailsService;

    @Resource
    AuthEntryPointJWT authEntryPointJWT;

    @Resource
    LogoutHandler logoutHandler;

    @Resource
    AlgoOAuth2UserService oauthUserService;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public AlgoAuthSuccessHandler authSuccessHandler(){
        return new AlgoAuthSuccessHandler();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        //設置角色定義
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpFirewall allowUrlSemicolonHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowSemicolon(true);//允許含有;的url
        return firewall;
    }

    /** 忽略api的url */
    public List<String> getApiIgnoredUrls(){
        return AlgoProperties.apiIgnoredUrls;
    }

    /** 忽略靜態資源的url */
    public List<String> getResourceUrls(){
        return AlgoProperties.resourceUrls;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http
                .authorizeRequests();
        //不需保護的路徑訪問
        getApiIgnoredUrls().forEach(a-> registry.antMatchers(a).permitAll());
        getResourceUrls().forEach(r->registry.antMatchers(r).permitAll());

        registry.and().cors().and().csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(authEntryPointJWT)
                .accessDeniedHandler(accessDeniedHandler())
                .and()
                .authorizeRequests()
                .antMatchers("/api/service/**").hasAuthority("1")
                .anyRequest().authenticated()
                .and()
                .logout().logoutUrl("/api/userLogout").addLogoutHandler(logoutHandler).logoutSuccessUrl("/index")
                .invalidateHttpSession(true)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //配置oauth2
        registry.and().oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(oauthUserService)
                .and()
                .successHandler(authSuccessHandler())
                .failureUrl("/403.html");

        //加filter
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
