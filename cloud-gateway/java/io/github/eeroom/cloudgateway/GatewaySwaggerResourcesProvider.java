package io.github.eeroom.cloudgateway;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

@Component
@Primary
public class GatewaySwaggerResourcesProvider implements SwaggerResourcesProvider {
    @Override
    public List<SwaggerResource> get() {
        var lst=new ArrayList<SwaggerResource>();
        lst.add(this.swaggerResource("cloud-kd","/cloud-kd/v2/api-docs","2.0"));
        lst.add(this.swaggerResource("151123456:cloud-oa","/151123456:cloud-oa/v2/api-docs","2.0"));
        return lst;
    }

    private SwaggerResource swaggerResource(String name, String location, String version) {
        var swaggerResource=new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}
