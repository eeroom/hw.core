package io.github.eeroom.gtop.hz.controller;

import io.github.eeroom.gtop.entity.ApidataWrapper;
import io.github.eeroom.gtop.entity.authen.LoginInput;
import io.github.eeroom.gtop.hz.AppConfig;
import io.github.eeroom.gtop.hz.authen.JwtTokenHelper;
import io.github.eeroom.gtop.hz.authen.SkipAuthentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class LoginController {
    AppConfig appconfig;
    public LoginController(AppConfig appconfig){
        this.appconfig=appconfig;
    }

    /**
     * 登陆
     * @param loginInput
     * @return
     */
    @SkipAuthentication
    public ResponseEntity<ApidataWrapper> signIn(LoginInput loginInput){
        if(loginInput.getAccount()==null ||loginInput.getAccount().length()<1)
            throw new RuntimeException("登陆用户名不能为空值");
        var token= JwtTokenHelper.encode(loginInput.getAccount());
        var apidata=new ApidataWrapper();
        apidata.setCode(HttpServletResponse.SC_OK);
        apidata.setMessage("登陆成功");
        apidata.setData(token);
        //使用ResponseEntity指定响应头、content-type，
        //下载文件的场景可以把文件流作为ResponseEntity的body
        var rt= ResponseEntity.ok().header(this.appconfig.authenJwtHeader,token)
                .body(apidata);
        return  rt;
    }

    /**
     * 注册
     * @param loginInput
     * @return
     */
    public ResponseEntity<ApidataWrapper> signUp(LoginInput loginInput){
        throw new IllegalAccessError("方法未完成");
    }

    /**
     * 注销登陆
     * @param loginInput
     * @return
     */
    public ResponseEntity<ApidataWrapper> signOut(LoginInput loginInput){
        throw new IllegalAccessError("方法未完成");
    }


}
