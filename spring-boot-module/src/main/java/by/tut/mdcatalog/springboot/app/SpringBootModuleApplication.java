package by.tut.mdcatalog.springboot.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;



@SpringBootApplication
@ComponentScan(basePackages = {
        "by.tut.mdcatalog.springboot.controller",
        "by.tut.mdcatalog.service",
        "by.tut.mdcatalog.data"
})
public class SpringBootModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootModuleApplication.class, args);
    }

}
