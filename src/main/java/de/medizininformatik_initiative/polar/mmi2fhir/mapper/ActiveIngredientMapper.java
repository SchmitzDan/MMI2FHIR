package de.medizininformatik_initiative.polar.mmi2fhir.mapper;

import de.medizininformatik_initiative.polar.mmi2fhir.model.Atc;
import de.medizininformatik_initiative.polar.mmi2fhir.model.MMIMolecule;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.Meta;
import org.springframework.stereotype.Component;

@Component
public class ActiveIngredientMapper {

  public Medication map(final MMIMolecule molecule, final Iterable<Atc> atcs) {
    final var medication = new Medication();

    // id
    medication.setId(DigestUtils.sha256Hex("MMIMolecule" + String.valueOf(molecule.getId())));

    // meta
    medication.setMeta(
        new Meta()
            .addProfile(
                "https://www.medizininformatik-initiative.de/fhir/core/modul-medikation/StructureDefinition/Medication")
            .setSource("https://www.mmi.de/mmi-pharmindex"));

    // identifier
    medication.addIdentifier()
        .setSystem("https://www.mmi.de/mmi-pharmindex/molecule")
        .setValue(String.valueOf(molecule.getId()));


    // code
    medication.setCode(
        new CodeableConcept()
            .setText(molecule.getName_plain()));

    for (final var atc : atcs) {
      medication.getCode()
          .addCoding(
              new Coding()
                  .setSystem("http://fhir.de/CodeSystem/dimdi/atc")
                  .setCode(atc.getCode())
                  .setDisplay(atc.getDescription()));
    }

    // ingredient
    // all active ingredients have either ask or cas number
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

    medication.addIngredient().setItem(cc);

    return medication;
  }
}
