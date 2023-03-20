package jp.co.axa.apidemo.config;

import org.springframework.context.annotation.Configuration;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

// Had to move @EnableSwagger2 to another config class because it caused a SpringFox BeanDefinitionException
// error for DataJpaTest if declared on main app class. This is because Spring does not load MVC context for 
// DataJpaTest
@EnableSwagger2
@Configuration
public class SwaggerConfig {

}
