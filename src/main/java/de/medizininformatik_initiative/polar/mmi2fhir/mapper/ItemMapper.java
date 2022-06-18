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
import org.hl7.fhir.r4.model.Ratio;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Type;
import org.springframework.stereotype.Component;

@Component
public class ItemMapper {

  public Medication map(final MMIItemCompressed item,
      final Iterable<MMICompositionElement> activeIngredients,
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
    // TODO: map item.getPharmformcode to http://hl7.org/fhir/uv/ips/ValueSet/medicine-doseform

    // amount TODO!
    // if (item.getBasecount() != null) {
    // medication.setAmount(new Ratio() // TODO: units!!!
    // .setNumerator(new Quantity()
    // .setValue(item.getBasecount())
    // .setSystem(null)
    // .setCode(null)
    // .setUnit(null))
    // .setDenominator(null)); // TODO!!!
    // }

    // ingredient
    for (final MMICompositionElement ingredient : activeIngredients) {
      final var ingredientComponent = new MedicationIngredientComponent();
      ingredientComponent
          .setItem(new Reference()
              .setIdentifier(
                  new Identifier()
                      .setSystem("https://www.mmi.de/mmi-pharmindex/molecule")
                      .setValue(String.valueOf(ingredient.getId()))))
          .setIsActive(true);

      if (ingredient.getMassfrom() != null) {
        // TODO: set Strength
      }

      medication.addIngredient(ingredientComponent);
    }

    for (final var pair : ingredients) {
      final var compositionElement = pair.getLeft();
      final var molecule = pair.getRight();

      Type value;

      if (StringUtils.isBlank(molecule.getAsknumber())
          && StringUtils.isBlank(molecule.getCasregistrationnumber())) {
        // either ask or cas provided, so we need to use a reference as a coding is
        // mandantory when using codeableConcept
        value = new Reference()
            .setType("Substance")
            .setIdentifier(new Identifier()
                .setSystem("https://www.mmi.de/mmi-pharmindex/molecule")
                .setValue(String.valueOf(molecule.getId())));
      } else {
        final var cc = new CodeableConcept()
            .setText(molecule.getName_plain());
        // ask
        if (StringUtils.isNotBlank(molecule.getAsknumber())) {
          cc.addCoding()
              .setCode(molecule.getAsknumber())
              .setSystem("http://fhir.de/CodeSystem/ask");
        }
        // cas
        if (StringUtils.isNotBlank(molecule.getCasregistrationnumber())) {
          cc.addCoding()
              .setCode(molecule.getCasregistrationnumber())
              .setSystem("urn:oid:2.16.840.1.113883.6.61");
        }
        value = cc;
      }

      medication.addIngredient()
          .setItem(value)
          .setIsActive(false)
          .setStrength(new Ratio()
              .setNumerator(null) // TODO! What about massfrom is null?
              .setDenominator(null)); // TODO!
    }

    return medication;
  }
}
