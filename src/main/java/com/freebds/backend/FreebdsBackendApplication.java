package com.freebds.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@SpringBootApplication
@EnableSwagger2WebMvc
// TODO check impact of commenting out the following annotation
// Google : @pageabledefault parameters name error
// https://github.com/springfox/springfox/issues/2623
// cf post from MiguelAngelLV on 12/12/18
@Import(SpringDataRestConfiguration.class)
public class FreebdsBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FreebdsBackendApplication.class, args);
    }

}
