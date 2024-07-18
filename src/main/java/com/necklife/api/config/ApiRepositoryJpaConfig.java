package com.necklife.api.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
@EnableJpaRepositories(
		basePackages = ApiAppConfig.BASE_PACKAGE,
		transactionManagerRef = ApiRepositoryJpaConfig.TRANSACTION_MANAGER_NAME,
		entityManagerFactoryRef = ApiRepositoryEntityConfig.ENTITY_MANAGER_FACTORY_NAME)
public class ApiRepositoryJpaConfig {

	public static final String TRANSACTION_MANAGER_NAME =
			ApiAppConfig.BEAN_NAME_PREFIX + "TransactionalManager";

	@Bean(name = TRANSACTION_MANAGER_NAME)
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}
}
