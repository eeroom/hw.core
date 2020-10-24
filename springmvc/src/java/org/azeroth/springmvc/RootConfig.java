package org.azeroth.springmvc;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan(excludeFilters ={@ComponentScan.Filter(value = EnableWebMvc.class)})
public class RootConfig {
}
