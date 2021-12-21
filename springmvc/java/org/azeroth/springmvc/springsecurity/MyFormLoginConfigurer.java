package org.azeroth.springmvc.springsecurity;

import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.web.authentication.ForwardAuthenticationFailureHandler;
import org.springframework.security.web.authentication.ForwardAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class MyFormLoginConfigurer <H extends HttpSecurityBuilder<H>> extends AbstractAuthenticationFilterConfigurer<H, MyFormLoginConfigurer<H>, MyUsernamePasswordAuthenticationFilter> {
    public MyFormLoginConfigurer() {
        super(new MyUsernamePasswordAuthenticationFilter(), (String)null);
        this.usernameParameter("username");
        this.passwordParameter("password");
    }

    public MyFormLoginConfigurer<H> loginPage(String loginPage) {
        return (MyFormLoginConfigurer)super.loginPage(loginPage);
    }

    public MyFormLoginConfigurer<H> usernameParameter(String usernameParameter) {
        ((UsernamePasswordAuthenticationFilter)this.getAuthenticationFilter()).setUsernameParameter(usernameParameter);
        return this;
    }

    public MyFormLoginConfigurer<H> passwordParameter(String passwordParameter) {
        ((UsernamePasswordAuthenticationFilter)this.getAuthenticationFilter()).setPasswordParameter(passwordParameter);
        return this;
    }

    public MyFormLoginConfigurer<H> failureForwardUrl(String forwardUrl) {
        this.failureHandler(new ForwardAuthenticationFailureHandler(forwardUrl));
        return this;
    }

    public MyFormLoginConfigurer<H> successForwardUrl(String forwardUrl) {
        this.successHandler(new ForwardAuthenticationSuccessHandler(forwardUrl));
        return this;
    }

    public void init(H http) throws Exception {
        super.init(http);
        this.initDefaultLoginFilter(http);
    }

    protected RequestMatcher createLoginProcessingUrlMatcher(String loginProcessingUrl) {
        return new AntPathRequestMatcher(loginProcessingUrl, "POST");
    }

    private String getUsernameParameter() {
        return ((UsernamePasswordAuthenticationFilter)this.getAuthenticationFilter()).getUsernameParameter();
    }

    private String getPasswordParameter() {
        return ((UsernamePasswordAuthenticationFilter)this.getAuthenticationFilter()).getPasswordParameter();
    }

    private void initDefaultLoginFilter(H http) {
        DefaultLoginPageGeneratingFilter loginPageGeneratingFilter = (DefaultLoginPageGeneratingFilter)http.getSharedObject(DefaultLoginPageGeneratingFilter.class);
        if (loginPageGeneratingFilter != null && !this.isCustomLoginPage()) {
            loginPageGeneratingFilter.setFormLoginEnabled(true);
            loginPageGeneratingFilter.setUsernameParameter(this.getUsernameParameter());
            loginPageGeneratingFilter.setPasswordParameter(this.getPasswordParameter());
            loginPageGeneratingFilter.setLoginPageUrl(this.getLoginPage());
            loginPageGeneratingFilter.setFailureUrl(this.getFailureUrl());
            loginPageGeneratingFilter.setAuthenticationUrl(this.getLoginProcessingUrl());
        }

    }
}
