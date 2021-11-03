package org.azeroth.workflow.controller;

import com.auth0.jwt.algorithms.Algorithm;
import org.azeroth.workflow.ApidataWrapper;
import org.azeroth.workflow.HttpPost;
import org.azeroth.workflow.SkipAuthentication;
import org.azeroth.workflow.viewModel.LoginInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestController
public class Account {
    @Autowired
    HttpServletResponse response;

    @SkipAuthentication
    @HttpPost
    public ApidataWrapper login(LoginInput loginInput){
        var token= com.auth0.jwt.JWT.create()
                .withIssuer("workflow")
                .withIssuedAt(new Date())
                .withClaim("userName",loginInput.getLoginName())
                .sign(Algorithm.HMAC256("hw@123456"));
        response.setHeader("Authorization",token);
        var rw=new ApidataWrapper();
        rw.setMessage("登陆成功");
        return  rw;
    }
}
