package io.github.eeroom.sf;

import org.springframework.beans.factory.annotation.Value;

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
}
