package ft.projects.planner.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Planner Application").version("1.0").description("API Documentation"))
                .components(new Components().addSecuritySchemes("BasicAuth", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .in(SecurityScheme.In.HEADER)
                        .scheme("basic")))
                .paths(new Paths()
                        .addPathItem("/api/users/logout", new PathItem()
                                .post(new Operation().tags(List.of("user"))
                                        .summary("Logout")
                                        .description("Logs out the current user from the application")
                                        .operationId("logoutUser")
                                        .responses(new ApiResponses()
                                                .addApiResponse("200", new ApiResponse().description("Successfully logged out"))
                                                .addApiResponse("403", new ApiResponse().description("Invalid Csrf Token"))
                                        )
                                        .parameters(List.of(new Parameter().name("X-XSRF-TOKEN").description("Csrf Token Header").required(true).allowEmptyValue(true).in("header")))
                                )
                        )
                );
    }
}
