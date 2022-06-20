package de.medizininformatik_initiative.polar.mmi2fhir.writer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.listener.RetryListenerSupport;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import io.micrometer.core.instrument.Metrics;

@Component
@ConditionalOnProperty(
    value = "data.writeToFile",
    havingValue = "false",
    matchIfMissing = true)
public class FhirBundleWriter implements ItemWriter<BundleEntryComponent> {
  private static final AtomicInteger resourcesWrittenTotal =
      Metrics.globalRegistry.gauge("batch.fhir.resources.written.total", new AtomicInteger(0));
  private static final AtomicInteger bundlesWrittenTotal =
      Metrics.globalRegistry.gauge("batch.fhir.bundles.written.total", new AtomicInteger(0));

  private final Logger log = LoggerFactory.getLogger(FhirBundleWriter.class);
  private final IGenericClient client;
  private final RetryTemplate retryTemplate = new RetryTemplate();

  @Autowired
  public FhirBundleWriter(final IGenericClient fhirClient) {
    this.client = fhirClient;

    // this.fhirJsonParser = fhirClient.getFhirContext().newJsonParser();

    final var backOffPolicy = new FixedBackOffPolicy();
    backOffPolicy.setBackOffPeriod(10000);
    final var retryPolicy = new SimpleRetryPolicy();
    retryPolicy.setMaxAttempts(10);

    retryTemplate.setBackOffPolicy(backOffPolicy);
    retryTemplate.setRetryPolicy(retryPolicy);

    retryTemplate.registerListener(new RetryListenerSupport() {
      @Override
      public <T, E extends Throwable> void onError(final RetryContext context,
          final RetryCallback<T, E> callback,
          final Throwable throwable) {
        log.warn("Trying to send bundle caused error {}. {}. attempt.",
            throwable.getMessage(),
            context.getRetryCount());
      }
    });
  }

  @Override
  public void write(final List<? extends BundleEntryComponent> items) {

    final var bundle = new Bundle();
    bundle.setType(BundleType.BATCH);
    for (final var item : items) {
      bundle.addEntry(item);
    }

    try {

      final var response = retryTemplate.execute((RetryCallback<Bundle, Exception>) context -> {
        context.setAttribute("request", bundle);
        return client.transaction().withBundle(bundle).execute();
      });

      resourcesWrittenTotal.addAndGet(bundle.getEntry().size());

    } catch (final Exception e) {
      log.error("Sending bundle failed", e);
    }

    final var written = bundlesWrittenTotal.addAndGet(1);
    log.info("Wrote a total of {} bundles ({} resources) to the server", written,
        resourcesWrittenTotal.get());
  }
}
