package io.github.eeroom.cloudoa.service.cloudkd;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("cloud-kd")
public interface IHome {
    @GetMapping("home/say")
    String say();
}
