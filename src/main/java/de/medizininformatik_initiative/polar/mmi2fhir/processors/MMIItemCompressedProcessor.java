package de.medizininformatik_initiative.polar.mmi2fhir.processors;

import de.medizininformatik_initiative.polar.mmi2fhir.dao.MMIDao;
import de.medizininformatik_initiative.polar.mmi2fhir.mapper.ItemMapper;
import de.medizininformatik_initiative.polar.mmi2fhir.model.MMIItemCompressed;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Bundle.BundleEntryRequestComponent;
import org.hl7.fhir.r4.model.Bundle.HTTPVerb;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MMIItemCompressedProcessor
    implements ItemProcessor<MMIItemCompressed, BundleEntryComponent> {

  private final ItemMapper mapper;
  private final MMIDao dao;

  public MMIItemCompressedProcessor(final ItemMapper mapper, final MMIDao dao) {
    super();
    this.mapper = mapper;
    this.dao = dao;
  }

  @Override
  public BundleEntryComponent process(final MMIItemCompressed item) throws Exception {

    // by definition of the "compression" of the items all items should have the same
    // compositionElements
    final var compositionElements = dao.loadCompositionElementsForItem(item.getIds().get(0));

    log.trace("Found {} compositionelements for item with id {}", compositionElements.size(),
        item.getIds().get(0));

    final var activeIngredients = compositionElements.stream()
        .filter(x -> "A".equals(x.getMoleculetypecode()))
        .map(x -> Pair.of(x, dao.loadMolecule(x.getMoleculeid())))
        .collect(Collectors.toList());

    log.trace("Identified {} compositionelements as active ingredients for item with id {}",
        activeIngredients.size(),
        item.getIds().get(0));

    final var otherIngredients = compositionElements.stream()
        .filter(x -> !"A".equals(x.getMoleculetypecode()))
        .map(x -> Pair.of(x, dao.loadMolecule(x.getMoleculeid())))
        .collect(Collectors.toList());

    log.trace("Identified {} compositionelements as other ingredients for item with id {}",
        otherIngredients.size(),
        item.getIds().get(0));

    final var atcs = dao.loadAtcsForItem(item.getIds().get(0));

    log.trace("Found {} ATCs  for item with id {}",
        atcs.size(),
        item.getIds().get(0));

    final var medicationResource = mapper.map(item, activeIngredients, otherIngredients, atcs);

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
