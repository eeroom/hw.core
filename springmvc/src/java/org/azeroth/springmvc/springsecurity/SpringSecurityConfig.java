package org.azeroth.springmvc.springsecurity;

import org.azeroth.springmvc.ApiResult;
import org.azeroth.springmvc.springsecurity.MyFormLoginConfigurer;
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

//@Configuration
//@EnableWebSecurity
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
                .failureHandler(this)
                .and()
                .authorizeRequests().antMatchers("/account/**","/Assets/**","/assets/**").permitAll()
                .and()
                .authorizeRequests().antMatchers("/**").authenticated()
                .and()
                .rememberMe().tokenValiditySeconds(60*60*1000).key("_wch_")
                .and()
                .logout().logoutUrl("/account/logout")
                .and()
                .csrf().disable();
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
        var rt=new ApiResult<String>();
        rt.setCode(200);
        //应该从请求的url中获取原来的目标地址，而不是全部设为/
        rt.setData("/");
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(rt));
        httpServletResponse.getWriter().flush();
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        var objectMapper=new com.fasterxml.jackson.databind.ObjectMapper();
        var rt=new ApiResult<String>();
        rt.setCode(500);
        rt.setMsg(e.getLocalizedMessage());
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(rt));
        httpServletResponse.getWriter().flush();
    }
}
