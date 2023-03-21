package jp.co.axa.apidemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Main application class
 * 
 * @author Arthur Campos Costa
 */
@EnableCaching
@SpringBootApplication
public class ApiDemoApplication {

	/**
	 * Application entry point
	 * 
	 * @param args command line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(ApiDemoApplication.class, args);
	}
}
