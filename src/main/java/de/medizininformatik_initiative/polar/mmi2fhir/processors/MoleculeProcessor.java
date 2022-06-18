package de.medizininformatik_initiative.polar.mmi2fhir.processors;

import de.medizininformatik_initiative.polar.mmi2fhir.mapper.MoleculeMapper;
import de.medizininformatik_initiative.polar.mmi2fhir.model.MMIMolecule;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleEntryRequestComponent;
import org.hl7.fhir.r4.model.Bundle.HTTPVerb;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class MoleculeProcessor implements ItemProcessor<MMIMolecule, BundleEntryComponent> {

  private final MoleculeMapper mapper;

  public MoleculeProcessor(final MoleculeMapper mapper) {
    super();
    this.mapper = mapper;
  }

  @Override
  public BundleEntryComponent process(final MMIMolecule item) throws Exception {

    final var substanceResource = mapper.map(item);

    final var url = "Substance/" + substanceResource.getId();

    return new BundleEntryComponent()
        .setFullUrl(url)
        .setResource(substanceResource)
        .setRequest(
            new BundleEntryRequestComponent()
                .setMethod(HTTPVerb.PUT)
                .setUrl(url));

  }
}
