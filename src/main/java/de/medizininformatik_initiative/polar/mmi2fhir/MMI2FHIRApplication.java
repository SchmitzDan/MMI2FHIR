package de.medizininformatik_initiative.polar.mmi2fhir;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MMI2FHIRApplication {

  public static void main(final String[] args) {
    final SpringApplication application = new SpringApplication(MMI2FHIRApplication.class);
    application.setWebApplicationType(WebApplicationType.NONE);
    application.run(args);

  }
}
