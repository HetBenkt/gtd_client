package nl.bos.gtd.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "nl.bos.gtd.server.repositories")
@EntityScan(basePackages = "nl.bos.gtd.server.entities")
public class GtdClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(GtdClientApplication.class, args);
	}
}
