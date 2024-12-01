package br.com.contafacil.bonnarotec.registry;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EntityScan(basePackages = {"br.com.contafacil.shared.bonnarotec.toolslib.domain"})
@OpenAPIDefinition(
    info = @Info(
        title = "Registry Service API",
        version = "1.0",
        description = "API for managing clients and users"
    )
)
public class RegistryApplication {
    public static void main(String[] args) {
        SpringApplication.run(RegistryApplication.class, args);
    }
}
