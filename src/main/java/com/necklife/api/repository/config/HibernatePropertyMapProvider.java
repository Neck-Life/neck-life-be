package com.necklife.api.repository.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class HibernatePropertyMapProvider {

	private final HibernateProperties hibernateProperties;
	private final JpaProperties jpaProperties;

	public HibernatePropertyMapProvider(
			@Qualifier(com.necklife.api.repository.config.DataJpaConfig.HIBERNATE_PROPERTIES) HibernateProperties hibernateProperties,
			@Qualifier(com.necklife.api.repository.config.DataJpaConfig.JPA_PROPERTIES) JpaProperties jpaProperties) {
		this.hibernateProperties = hibernateProperties;
		this.jpaProperties = jpaProperties;
	}

	public Map<String, Object> get() {
		return hibernateProperties.determineHibernateProperties(
				jpaProperties.getProperties(), new HibernateSettings());
	}
}
