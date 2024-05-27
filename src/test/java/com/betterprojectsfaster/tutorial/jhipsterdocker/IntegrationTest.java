package com.betterprojectsfaster.tutorial.jhipsterdocker;

import com.betterprojectsfaster.tutorial.jhipsterdocker.config.AsyncSyncConfiguration;
import com.betterprojectsfaster.tutorial.jhipsterdocker.config.EmbeddedSQL;
import com.betterprojectsfaster.tutorial.jhipsterdocker.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { MySimpleShopApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedSQL
public @interface IntegrationTest {
}
