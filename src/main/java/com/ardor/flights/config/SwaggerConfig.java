package com.ardor.flights.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Value("${springdoc.Local_URL}")
    private String URL;
    @Value("${springdoc.AWS_URL}")
    private String URL_QA;
    @Bean
    public OpenAPI openAPI()
    {
        ApiResponse BadRequest = new ApiResponse()
                .description("Invalid Request");

        ApiResponse InternalServerError = new ApiResponse()
                .description("Service failure, Please contact API admin team");

        ApiResponse BadGateway = new ApiResponse()
                .description("Bad Gateway");

        ApiResponse GatewayTimeout = new ApiResponse()
                .description("Gateway Timeout");

        ApiResponse ServiceUnavailable = new ApiResponse()
                .description("Service is temporarily unavailable, please try after some time");

        Components components = new Components();

        components.addResponses("BAD_REQUEST", BadRequest);
        components.addResponses("INTERNAL_SERVER_ERROR",InternalServerError);
        components.addResponses("BAD_GATE_WAY",BadGateway);
        components.addResponses("GATEWAY_TIMEOUT",GatewayTimeout);
        components.addResponses("SERVICE_UNAVAILABLE",ServiceUnavailable);


        return new OpenAPI()
                .info(new Info()
                        .title("Flights-API")
                        .description("Flights API provides end points to deal with operations like search , re-price, book , read PNR, read ticket, order ticket, cancel , manage ancillaries and manage fare rules. You need to acquire an API key to use these end points and follow a few guidelines as per Ardor Guidelines. You can refer to our documentation site for more details.")
                        .version("1.0")
                        .termsOfService("Need to Add URL")
                        .contact(new Contact()
                                .email("mkkumar@ardortravels.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0"))
                )
                .servers(
                        List.of(new Server().url(URL).description("Local_Server")
                        ,new Server().url(URL_QA).description("QA_Server")))
                .components(components);

    }
}
