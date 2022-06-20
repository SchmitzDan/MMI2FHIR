package de.medizininformatik_initiative.polar.mmi2fhir;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;

@Configuration
public class FhirConfig {

  @Bean
  public IGenericClient fhirClient(@Value("${fhir.url}") final String url) {
    final var ctx = FhirContext.forR4();
    ctx.getRestfulClientFactory().setConnectTimeout(30 * 1000);
    ctx.getRestfulClientFactory().setSocketTimeout(30 * 1000);
    ctx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);

    final var client = ctx.newRestfulGenericClient(url);

    return client;
  }
}
