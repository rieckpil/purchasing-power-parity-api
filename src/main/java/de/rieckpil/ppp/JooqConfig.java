package de.rieckpil.ppp;

import io.r2dbc.spi.ConnectionFactory;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig {
  @Bean
  public DSLContext dslContext(ConnectionFactory connectionFactory) {
    return DSL.using(
        new DefaultConfiguration().derive(connectionFactory).derive(SQLDialect.POSTGRES));
  }
}
