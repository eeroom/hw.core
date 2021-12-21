package io.github.eeroom.sf.controller;

import com.auth0.jwt.algorithms.Algorithm;
import io.github.eeroom.entity.ApidataWrapper;
import io.github.eeroom.entity.LoginInput;
import io.github.eeroom.sf.HttpPost;
import io.github.eeroom.sf.SkipAuthentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestController
public class Account {
    @SkipAuthentication
    @HttpPost
    public ResponseEntity<ApidataWrapper> login(LoginInput loginInput){
        var token= com.auth0.jwt.JWT.create()
                .withIssuer("workflow")
                .withIssuedAt(new Date())
                .withClaim("userName",loginInput.getLoginName())
                .sign(Algorithm.HMAC256("hw@123456"));
        var apidata=new ApidataWrapper();
        apidata.setCode(HttpServletResponse.SC_OK);
        apidata.setMessage("登陆成功");
        apidata.setData(token);
        //使用ResponseEntity指定响应头、content-type，
        //下载文件的场景可以把文件流作为ResponseEntity的body
        var rt= ResponseEntity.ok().header("Authorization",token)
                .body(apidata);
        return  rt;
    }
}
