package org.azeroth.springmvc;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //copy默认实现，改成默认实现，增加其他校验因素，比如图片验证码，手机短信验证码
        //自定义的校验逻辑
        response.setCharacterEncoding("UTF-8");
        String username = this.obtainUsername(request);
        String password = this.obtainPassword(request);
        if (username == null) {
            username = "";
        }

        if (password == null) {
            password = "";
        }
        //增加校验验证码
        if("eeroom2".equals(username)){
            throw new AuthenticationException("校验码错误"){};
        }
        username = username.trim();
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        this.setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
