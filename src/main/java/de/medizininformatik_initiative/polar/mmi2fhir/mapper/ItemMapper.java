package de.medizininformatik_initiative.polar.mmi2fhir.mapper;

import de.medizininformatik_initiative.polar.mmi2fhir.model.Atc;
import de.medizininformatik_initiative.polar.mmi2fhir.model.MMICompositionElement;
import de.medizininformatik_initiative.polar.mmi2fhir.model.MMIItemCompressed;
import de.medizininformatik_initiative.polar.mmi2fhir.model.MMIMolecule;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.Medication.MedicationIngredientComponent;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Ratio;
import org.hl7.fhir.r4.model.Reference;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {

  public Medication map(final MMIItemCompressed item,
      final Iterable<Pair<MMICompositionElement, MMIMolecule>> activeIngredients,
      final Iterable<Pair<MMICompositionElement, MMIMolecule>> ingredients,
      final Iterable<Atc> atcs) {
    final var medication = new Medication();

    // id
    medication.setId(DigestUtils.sha256Hex("MMIItem" + StringUtils.join(item.getIds(), ',')));

    // meta
    medication.setMeta(
        new Meta()
            .addProfile(
                "https://www.medizininformatik-initiative.de/fhir/core/modul-medikation/StructureDefinition/Medication")
            .setSource("https://www.mmi.de/mmi-pharmindex"));

    // identifier
    for (final Integer id : item.getIds()) {
      medication.addIdentifier()
          .setSystem("https://www.mmi.de/mmi-pharmindex/item")
          .setValue(String.valueOf(id));
    }

    // code
    medication.setCode(
        new CodeableConcept()
            .setText(item.getName()));

    for (final var atc : atcs) {
      medication.getCode()
          .addCoding(
              new Coding()
                  .setSystem("http://fhir.de/CodeSystem/dimdi/atc")
                  .setCode(atc.getCode())
                  .setDisplay(atc.getDescription()));
    }

    // form
    if (StringUtils.isNotBlank(item.getPharmformcode())) {
      medication.setForm(new CodeableConcept()
          .addCoding(new Coding()
              .setCode(item.getPharmformcode())
              .setSystem(
                  "https://www.mmi.de/mmi-pharmindex/catalog/" + item.getPharmformcatalogid())));
    }

    // amount
    if (item.getMainbasecount() != null) {
      medication.setAmount(new Ratio()
          .setNumerator(new Quantity()
              .setValue(item.getMainbasecount().doubleValue())
              .setCode(item.getMainbasemoleculeunitcode())
              .setSystem(item.getMainbasemoleculeunitcatalogid() == null ? null
                  : "https://www.mmi.de/mmi-pharmindex/catalog/"
                      + item.getMainbasemoleculeunitcatalogid()))
          .setDenominator(new Quantity(1)));
    } else if (item.getBasecount() != null) {
      medication.setAmount(new Ratio()
          .setNumerator(new Quantity()
              .setValue(item.getBasecount().doubleValue())
              .setCode(item.getBasemoleculeunitcode())
              .setSystem(item.getBasemoleculeunitcatalogid() == null ? null
                  : "https://www.mmi.de/mmi-pharmindex/catalog/"
                      + item.getBasemoleculeunitcatalogid()))
          .setDenominator(new Quantity(1)));
    }

    // ingredient
    // active
    final var baseQuantity = item.getBasecount() == null ? new Quantity(1)
        : new Quantity()
            .setValue(item.getBasecount().doubleValue())
            .setCode(item.getBasemoleculeunitcode())
            .setSystem(item.getBasemoleculeunitcatalogid() == null ? null
                : "https://www.mmi.de/mmi-pharmindex/catalog/"
                    + item.getBasemoleculeunitcatalogid());

    for (final var ingredient : activeIngredients) {
      final var compositionElement = ingredient.getLeft();
      final var molecule = ingredient.getRight();

      final var ingredientComponent = new MedicationIngredientComponent();
      ingredientComponent
          .setItem(new Reference()
              .setDisplay(molecule.getName_plain())
              .setIdentifier(
                  new Identifier()
                      .setSystem("https://www.mmi.de/mmi-pharmindex/molecule")
                      .setValue(String.valueOf(molecule.getId()))))
          .setIsActive(true);

      if (compositionElement.getMassfrom() != null) {
        ingredientComponent.setStrength(new Ratio()
            .setNumerator(new Quantity()
                .setValue(compositionElement.getMassfrom())
                .setCode(compositionElement.getMoleculeunitcode())
                .setSystem(compositionElement.getMoleculeunitcatalogid() == null ? null
                    : "https://www.mmi.de/mmi-pharmindex/catalog/"
                        + compositionElement.getMoleculeunitcatalogid()))
            .setDenominator(baseQuantity));
      }

      medication.addIngredient(ingredientComponent);
    }

    // other
    for (final var pair : ingredients) {

      final var ingredientComponent = new MedicationIngredientComponent();

      final var compositionElement = pair.getLeft();
      final var molecule = pair.getRight();

      if (StringUtils.isBlank(molecule.getAsknumber())
          && StringUtils.isBlank(molecule.getCasregistrationnumber())) {
        // neither ask nor cas provided, so we need to use a reference as a coding is
        // mandantory when using codeableConcept
        ingredientComponent.setItem(new Reference()
            .setType("Substance")
            .setIdentifier(new Identifier()
                .setSystem("https://www.mmi.de/mmi-pharmindex/molecule")
                .setValue(String.valueOf(molecule.getId()))));
      } else {
        ingredientComponent.setItem(new CodeableConcept()
            .setText(molecule.getName_plain()));

        // ask
        if (StringUtils.isNotBlank(molecule.getAsknumber())) {
          ingredientComponent.getItemCodeableConcept()
              .addCoding()
              .setCode(molecule.getAsknumber())
              .setSystem("http://fhir.de/CodeSystem/ask");
        }
        // cas
        if (StringUtils.isNotBlank(molecule.getCasregistrationnumber())) {
          ingredientComponent.getItemCodeableConcept()
              .addCoding()
              .setCode(molecule.getCasregistrationnumber())
              .setSystem("urn:oid:2.16.840.1.113883.6.61");
        }
      }

      if (compositionElement.getMassfrom() != null) {
        ingredientComponent.setStrength(new Ratio()
            .setNumerator(new Quantity()
                .setValue(compositionElement.getMassfrom())
                .setCode(compositionElement.getMoleculeunitcode())
                .setSystem(compositionElement.getMoleculeunitcatalogid() == null ? null
                    : "https://www.mmi.de/mmi-pharmindex/catalog/"
                        + compositionElement.getMoleculeunitcatalogid()))
            .setDenominator(baseQuantity));
      }

      medication.addIngredient(ingredientComponent);
    }

    return medication;
  }
}
