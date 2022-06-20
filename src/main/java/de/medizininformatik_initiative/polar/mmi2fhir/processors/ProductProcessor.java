package de.medizininformatik_initiative.polar.mmi2fhir.processors;

import de.medizininformatik_initiative.polar.mmi2fhir.dao.MMIDao;
import de.medizininformatik_initiative.polar.mmi2fhir.mapper.ProductMapper;
import de.medizininformatik_initiative.polar.mmi2fhir.model.MMIProduct;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleEntryRequestComponent;
import org.hl7.fhir.r4.model.Bundle.HTTPVerb;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ProductProcessor implements ItemProcessor<MMIProduct, BundleEntryComponent> {

  private final ProductMapper mapper;
  private final MMIDao dao;

  public ProductProcessor(final ProductMapper mapper, final MMIDao dao) {
    super();
    this.mapper = mapper;
    this.dao = dao;
  }

  @Override
  public BundleEntryComponent process(final MMIProduct item) throws Exception {

    final var mmiItems = dao.loadItemsForProduct(item.getId());

    final var medicationResource = mapper.map(item, mmiItems);

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
