package com.example.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EcomerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcomerceApplication.class, args);
		// TODO: CODE REVIEW - Substituir System.err.println por logger apropriado
		// Sugestão: Usar LoggerFactory.getLogger() com nível INFO
		// Benefício: Padroniza logging, facilita monitoramento e melhora profissionalismo
		System.err.println("Bora que começou ein");
	}

}
