package jp.co.axa.apidemo.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

// Had to move @EnableSwagger2 to another config class because it caused a SpringFox BeanDefinitionException
// error for DataJpaTest if declared on main app class. This is because Spring does not load MVC context for 
// DataJpaTest
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    /**
     * Docket configuration bean
     * 
     * @return
     */
    @Bean
    Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                // disable default response messages
                .select()
                // specify path where APIs are exposed (exclude CustomErrorController)
                .paths(PathSelectors.ant("/api/v1/**"))
                .build()
                .useDefaultResponseMessages(false)
                .apiInfo(
                        new ApiInfo(
                                "Employee API",
                                "API for AXA coding test",
                                "1.0",
                                "Free to use",
                                new Contact("Arthur Campos Costa", "https://github.com/ArthurCC",
                                        "artelio.camposta@gmail.com"),
                                null,
                                null,
                                Collections.emptyList()));
    }
}
