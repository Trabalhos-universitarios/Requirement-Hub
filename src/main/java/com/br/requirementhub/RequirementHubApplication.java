package com.br.requirementhub;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Requirement Hub API", version = "1.0.0",
		description = "Documentation Requirement Hub API v1.0"))
public class RequirementHubApplication {

	public static void main(String[] args) {
		SpringApplication.run(RequirementHubApplication.class, args);
	}

}
