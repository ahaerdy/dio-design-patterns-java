package one.digitalinnovation.gofspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Ponto de entrada da aplicação Spring Boot.
 *
 * @SpringBootApplication: ativa toda a mágica do Spring —
 * varredura de componentes, autoconfiguração, etc.
 *
 * @EnableFeignClients: diz ao Spring para procurar interfaces
 * anotadas com @FeignClient e gerar suas implementações.
 * Sem esta anotação, o ViaCepService não funcionaria.
 */
@SpringBootApplication
@EnableFeignClients
public class GofSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(GofSpringApplication.class, args);
	}
}
