package de.medizininformatik_initiative.polar.mmi2fhir.processors;

import de.medizininformatik_initiative.polar.mmi2fhir.mapper.PackageMapper;
import de.medizininformatik_initiative.polar.mmi2fhir.model.MMIPackage;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleEntryRequestComponent;
import org.hl7.fhir.r4.model.Bundle.HTTPVerb;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class PackageProcessor implements ItemProcessor<MMIPackage, BundleEntryComponent> {

  private final PackageMapper mapper;

  public PackageProcessor(final PackageMapper mapper) {
    super();
    this.mapper = mapper;
  }

  @Override
  public BundleEntryComponent process(final MMIPackage item) throws Exception {

    final var medicationResource = mapper.map(item);

    final var url = "Medication/" + medicationResource.getId();

    return new BundleEntryComponent()
        .setFullUrl(url)
        .setResource(medicationResource)
        .setRequest(
            new BundleEntryRequestComponent()
                .setMethod(HTTPVerb.PUT)
                .setUrl(url));
  }

}
