package Invernadero_servidor.invernadero;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(
	basePackages = {
	    "controller",
	    "service",
	    "repository",
	    "model",
	    "config",
	    "security",
	    "mqtt"
	},
	excludeFilters = @ComponentScan.Filter(
		type = FilterType.ASSIGNABLE_TYPE,
		classes = config.SecurityConfig.class
	)
)
@EntityScan(basePackages = "model")
@EnableJpaRepositories(basePackages = "repository")
@SpringBootApplication
public class InvernaderoApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvernaderoApplication.class, args);
	}

}
