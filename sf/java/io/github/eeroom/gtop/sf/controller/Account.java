package io.github.eeroom.gtop.sf.controller;

import com.auth0.jwt.algorithms.Algorithm;
import io.github.eeroom.gtop.entity.ApidataWrapper;
import io.github.eeroom.gtop.entity.authen.LoginInput;
import io.github.eeroom.gtop.sf.SkipAuthentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestController
public class Account {
    @SkipAuthentication
    public ResponseEntity<ApidataWrapper> login(LoginInput loginInput){
        var token= com.auth0.jwt.JWT.create()
                .withIssuer("workflow")
                .withIssuedAt(new Date())
                .withClaim("userName",loginInput.getAccount())
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
