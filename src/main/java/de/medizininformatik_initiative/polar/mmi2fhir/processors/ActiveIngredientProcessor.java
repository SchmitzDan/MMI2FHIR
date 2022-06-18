package de.medizininformatik_initiative.polar.mmi2fhir.processors;

import de.medizininformatik_initiative.polar.mmi2fhir.dao.MMIDao;
import de.medizininformatik_initiative.polar.mmi2fhir.mapper.ActiveIngredientMapper;
import de.medizininformatik_initiative.polar.mmi2fhir.model.MMIMolecule;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleEntryRequestComponent;
import org.hl7.fhir.r4.model.Bundle.HTTPVerb;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ActiveIngredientProcessor implements ItemProcessor<MMIMolecule, BundleEntryComponent> {

  private final ActiveIngredientMapper mapper;
  private final MMIDao dao;

  public ActiveIngredientProcessor(final ActiveIngredientMapper mapper, final MMIDao dao) {
    super();
    this.mapper = mapper;
    this.dao = dao;
  }

  @Override
  public BundleEntryComponent process(final MMIMolecule item) throws Exception {

    final var atcs = dao.loadAtcsForMolecule(item.getId());

    final var medicationResource = mapper.map(item, atcs);

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
