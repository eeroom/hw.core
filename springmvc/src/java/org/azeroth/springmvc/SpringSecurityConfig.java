package org.azeroth.springmvc;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter implements AuthenticationSuccessHandler, AuthenticationFailureHandler {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //super.configure(http);
        //这一句等价于默认的http.formLogin()，后续就使用默认的写法配置登陆的一些设置
        MyFormLoginConfigurer<HttpSecurity> formLoginConfigurer=new MyFormLoginConfigurer<>();
        http.apply(formLoginConfigurer).loginPage("/account/index")
                .usernameParameter("LoginName")
                .passwordParameter("Password")
                .loginProcessingUrl("/account/login")
                .successHandler(this)
                .failureHandler(this).permitAll();
        http.authorizeRequests().antMatchers("/account/*","/Assets/*").permitAll();
        http.authorizeRequests().antMatchers("/**").authenticated();
        http.rememberMe().tokenValiditySeconds(60*60*1000).key("_wch_");
        http.csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //super.configure(auth);
        auth.inMemoryAuthentication().withUser("eeroom").password("123456").roles("ADMIN");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        var objectMapper=new com.fasterxml.jackson.databind.ObjectMapper();
        LoginUserInfo loginUserInfo=new LoginUserInfo();
        loginUserInfo.setName(authentication.getName());
        var rt= objectMapper.writeValueAsString(loginUserInfo);
        httpServletResponse.getWriter().write(rt);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        var objectMapper=new com.fasterxml.jackson.databind.ObjectMapper();
        LoginUserInfo loginUserInfo=new LoginUserInfo();
        loginUserInfo.setName(e.getLocalizedMessage());
        var rt= objectMapper.writeValueAsString(loginUserInfo);
        httpServletResponse.getWriter().write(rt);
    }
}
