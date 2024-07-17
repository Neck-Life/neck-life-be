package com.necklife.api.config;

import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.SpringBeanContainer;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;

import static com.necklife.api.config.ApiRepositoryDataSourceConfig.DATASOURCE_NAME;


@Configuration
@RequiredArgsConstructor
public class ApiRepositoryEntityConfig {
	public static final String ENTITY_MANAGER_FACTORY_NAME =
			ApiAppConfig.BEAN_NAME_PREFIX + "EntityManagerFactory";
	private static final String PERSIST_UNIT =
			ApiAppConfig.BEAN_NAME_PREFIX + "PersistenceUnit";

	private final HibernatePropertyMapProvider hibernatePropertyMapProvider;

	@Bean(name = ENTITY_MANAGER_FACTORY_NAME)
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(
			@Qualifier(value = DATASOURCE_NAME) DataSource dataSource,
			EntityManagerFactoryBuilder builder,
			ConfigurableListableBeanFactory beanFactory) {

		LocalContainerEntityManagerFactoryBean build =
				builder
						.dataSource(dataSource)
						.properties(hibernatePropertyMapProvider.get())
						.persistenceUnit(PERSIST_UNIT)
						.packages(ApiAppConfig.BASE_PACKAGE)
						.build();
		build
				.getJpaPropertyMap()
				.put(AvailableSettings.BEAN_CONTAINER, new SpringBeanContainer(beanFactory));

		return build;
	}
}
