package de.medizininformatik_initiative.polar.mmi2fhir.mapper;

import de.medizininformatik_initiative.polar.mmi2fhir.model.MMIItem;
import de.medizininformatik_initiative.polar.mmi2fhir.model.MMIProduct;
import org.apache.commons.codec.digest.DigestUtils;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.Medication.MedicationIngredientComponent;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Ratio;
import org.hl7.fhir.r4.model.Reference;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

  public Medication map(final MMIProduct product, final Iterable<MMIItem> items) {
    final var medication = new Medication();

    // id
    medication.setId(DigestUtils.sha256Hex("MMIProduct" + product.getId()));

    // meta
    medication.setMeta(
        new Meta()
            .addProfile(
                "https://www.medizininformatik-initiative.de/fhir/core/modul-medikation/StructureDefinition/Medication")
            .setSource("https://www.mmi.de/mmi-pharmindex"));

    // identifier
    medication.addIdentifier()
        .setSystem("https://www.mmi.de/mmi-pharmindex/product")
        .setValue(String.valueOf(product.getId()));

    // code
    medication.setCode(
        new CodeableConcept()
            .setText(product.getName_plain()));

    // ingredients
    for (final var item : items) {
      final var ingredient = new MedicationIngredientComponent();

      ingredient
          .setItem(new Reference()
              .setDisplay(item.getName())
              .setType("Medication")
              .setIdentifier(
                  new Identifier()
                      .setSystem("https://www.mmi.de/mmi-pharmindex/item")
                      .setValue(String.valueOf(item.getId()))));

      // should this match the logic for medication.amount on item level?
      if (item.getMainbasecount() != null) {
        ingredient.setStrength(new Ratio()
            .setNumerator(new Quantity()
                .setValue(item.getMainbasecount().doubleValue())
                .setCode(item.getMainbasemoleculeunitcode())
                .setSystem(item.getMainbasemoleculeunitcatalogid() == null ? null
                    : "https://www.mmi.de/mmi-pharmindex/catalog/"
                        + item.getMainbasemoleculeunitcatalogid()))
            .setDenominator(new Quantity(1)));
      } else if (item.getBasecount() != null) {
        ingredient.setStrength(new Ratio()
            .setNumerator(new Quantity()
                .setValue(item.getBasecount().doubleValue())
                .setCode(item.getBasemoleculeunitcode())
                .setSystem(item.getBasemoleculeunitcatalogid() == null ? null
                    : "https://www.mmi.de/mmi-pharmindex/catalog/"
                        + item.getBasemoleculeunitcatalogid()))
            .setDenominator(new Quantity(1)));
      }
      medication.addIngredient(ingredient);
    }

    return medication;
  }
}
