package com.example.todolist.configs; // Ajuste o nome do pacote se for diferente

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Permite CORS para todas as rotas da aplicação
                .allowedOrigins("*")     // Permite requisições de qualquer origem (front-end)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Permite estes métodos HTTP
                .allowedHeaders("*");    // <-- A LINHA MAIS IMPORTANTE: Permite todos os cabeçalhos, incluindo Authorization
    }
}