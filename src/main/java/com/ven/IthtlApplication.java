package com.ven;

import com.ven.utils.ExtJpaRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = ExtJpaRepositoryFactoryBean.class)
public class IthtlApplication {

	public static void main(String[] args) {
		SpringApplication.run(IthtlApplication.class, args);
	}
}
