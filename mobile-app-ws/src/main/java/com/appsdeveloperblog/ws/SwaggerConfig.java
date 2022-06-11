package com.appsdeveloperblog.ws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		return new OpenAPI()
			.info(
					new Info()
						.title("Photo app RESTful Web Service documentation")
						.description("This pages documents Photo app RESTful web service")
						.version("1.0")
						.contact(new Contact()
							.email("test@test.com")
							.name("david")
							.url("test.com")
						)
						.termsOfService("Apache 2.0")
						.license(new License()
							.name("Apache 2.0")
							.url("http://www.apache.org/licenses,LICENSE-2.0")
								)
					);
	}
}
