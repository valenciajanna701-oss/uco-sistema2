package co.edu.uco.panel.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ucoOpenAPI() {
        return new OpenAPI().info(
                new Info()
                        .title("API UCO - Gestión Académica")
                        .version("2.0.0")
                        .description("""
                                Backend del panel académico de la **UCO** con Spring Boot y H2.

                                Reglas de color:
                                - **Color**: verde si el promedio es `>= 3`, rojo si es `< 3`.
                                - **Semáforo**: verde si es `>= 3.5`, amarillo entre `3.0` y `3.49`, rojo si es `< 3.0`.
                                """)
                        .contact(new Contact()
                                .name("Coordinación Académica UCO")
                                .email("academico@uco.edu.co")));
    }
}
