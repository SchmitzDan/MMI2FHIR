package de.medizininformatik_initiative.polar.mmi2fhir;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
public class DataSourceConfig {

  @Bean
  @Primary
  public DataSource defaultDataSource() {
    final var embeddedDatabaseBuilder = new EmbeddedDatabaseBuilder();

    return embeddedDatabaseBuilder
        .addScript("classpath:org/springframework/batch/core/schema-drop-h2.sql")
        .addScript("classpath:org/springframework/batch/core/schema-h2.sql")
        .setType(EmbeddedDatabaseType.H2)
        .build();
  }

  @Bean("mmiDataSource")
  public DataSource mmiDataSource() throws SQLException {

    final var embeddedDatabaseBuilder = new EmbeddedDatabaseBuilder();
    return embeddedDatabaseBuilder
        .addScript("classpath:MMI.sql")
        .setType(EmbeddedDatabaseType.H2).build();
  }
}
