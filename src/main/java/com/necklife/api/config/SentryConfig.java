package com.necklife.api.config;

import io.sentry.spring.jakarta.EnableSentry;
import org.springframework.context.annotation.Configuration;

@EnableSentry(
		dsn =
				"https://2ac12c73462c1a0b422af134c06374d5@o4507922386059264.ingest.de.sentry.io/4507922390319184")
@Configuration
class SentryConfig {}
