package io.github.eeroom.gtop.hz;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {
    @Value("${camundaJdbcDriver}")
    public String camundaJdbcDriver;

    @Value("${camundaJdbcUrl}")
    public String camundaJdbcUrl;

    @Value("${camundaJdbcUsername}")
    public String camundaJdbcUsername;

    @Value("${camundaJdbcPwd}")
    public String camundaJdbcPwd;

    @Value("${camundaDatabaseType}")
    public String camundaDatabaseType;

    @Value("${camundaDatabaseSchemaUpdate}")
    public String camundaDatabaseSchemaUpdate;

    @Value("${upload.tmpdir}")
    public String uploadtmpdir;
    @Value("${upload.maxUploadSize}")
    public int maxUploadSize;
    @Value("${upload.maxInMemorySize}")
    public int maxInMemorySize;

    @Value("${db.driver.n1}")
    public String dbdrivern1;
    @Value("${db.url.n1}")
    public String dburln1;
    @Value("${db.username.n1}")
    public String dbusernamen1;
    @Value("${db.pwd.n1}")
    public String dbpwdn1;

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

    @Value("${kuaidi.mycode}")
    public String kuaidimycode;

    @Value("${kuaidi.sf.url}")
    public String kuaidiSfUrl;

}
