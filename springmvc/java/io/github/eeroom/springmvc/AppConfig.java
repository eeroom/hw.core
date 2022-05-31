package io.github.eeroom.springmvc;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {
    @Value("${upload.tmpdir}")
    public String uploadtmpdir;
    @Value("${upload.maxUploadSize}")
    public int maxUploadSize;
    @Value("${upload.maxInMemorySize}")
    public int maxInMemorySize;


    @Value("${controller.path}")
    public String controllerPath;

    @Value("${authen.jwt.header}")
    public String authenJwtHeader;

    @Value("${authen.jwt.secret}")
    public String authenJwtSecret;

    @Value("${authen.jwt.issuer}")
    public String authenJwtIssuer;

    @Value("${swagger2.title}")
    public String swagger2title;

    @Value("${swagger2.description}")
    public String swagger2description;

    @Value("${swagger2.termsOfServiceUrl}")
    public String swagger2termsOfServiceUrl;

    @Value("${swagger2.version}")
    public String swagger2version;

    @Value("${swagger2.enable}")
    public Boolean swagger2enable;
}
