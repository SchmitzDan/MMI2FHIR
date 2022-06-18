package de.medizininformatik_initiative.polar.mmi2fhir.writer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleType;
import org.springframework.batch.item.ItemWriter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import io.micrometer.core.instrument.Metrics;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ConditionalOnProperty(
    value = "data.writeBundlesToFile",
    havingValue = "true",
    matchIfMissing = false)
public class FhirBundleToFileWriter implements ItemWriter<BundleEntryComponent> {
  private static final AtomicInteger resourcesWrittenTotal =
      Metrics.globalRegistry.gauge("batch.fhir.resources.written.total", new AtomicInteger(0));
  private static final AtomicInteger bundlesWrittenTotal =
      Metrics.globalRegistry.gauge("batch.fhir.bundles.written.total", new AtomicInteger(0));

  private final FhirContext ctx = FhirContext.forR4();
  private final IParser fhirJsonParser = ctx.newJsonParser().setPrettyPrint(true);
  private final Path fileOutDirectory = Paths.get("./bundle-out");

  public FhirBundleToFileWriter() {
    if (!Files.exists(fileOutDirectory)) {
      try {
        Files.createDirectories(fileOutDirectory);
      } catch (final IOException e) {
        log.error("Failed to create output dir for bundles", e);
      }
    }
  }

  @Override
  public void write(final List<? extends BundleEntryComponent> items) {

    final var bundle = new Bundle();
    bundle.setType(BundleType.BATCH);
    for (final var item : items) {
      bundle.addEntry(item);
    }

    final var bundleJson = fhirJsonParser.encodeResourceToString(bundle);
    final var outFilePath = fileOutDirectory
        .resolve(String.format("bundle-%d.json", bundlesWrittenTotal.incrementAndGet()));
    try {
      Files.write(outFilePath, bundleJson.getBytes());
    } catch (final IOException e) {
      log.warn("Failed to write bundle to file", e);
    }

    resourcesWrittenTotal.addAndGet(bundle.getEntry().size());

    log.info("Wrote a total of {} bundles ({} resources) to file", bundlesWrittenTotal.get(),
        resourcesWrittenTotal.get());
  }
}
