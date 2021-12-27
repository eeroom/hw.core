package io.github.eeroom.gtop.sf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MapProperties {
    @Value("${camundaJdbcDriver}")
    String camundaJdbcDriver;

    @Value("${camundaJdbcUrl}")
    String camundaJdbcUrl;

    @Value("${camundaJdbcUsername}")
    String camundaJdbcUsername;

    @Value("${camundaJdbcPwd}")
    String camundaJdbcPwd;

    @Value("${camundaDatabaseType}")
    String camundaDatabaseType;

    @Value("${camundaDatabaseSchemaUpdate}")
    String camundaDatabaseSchemaUpdate;

    @Value("${upload.tmpdir}")
    String uploadtmpdir;
    @Value("${upload.maxUploadSize}")
    int maxUploadSize;
    @Value("${upload.maxInMemorySize}")
    int maxInMemorySize;

    @Value("${db.sf.driver}")
    String dbsfdriver;
    @Value("${db.sf.url}")
    String dbsfurl;
    @Value("${db.sf.username}")
    String dbsfusername;
    @Value("${db.sf.pwd}")
    String dbsfpwd;
}
