package de.medizininformatik_initiative.polar.mmi2fhir.mapper;

import de.medizininformatik_initiative.polar.mmi2fhir.model.MMIMolecule;
import org.apache.commons.codec.digest.DigestUtils;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Substance;
import org.springframework.stereotype.Component;

@Component
public class MoleculeMapper {

  public Substance map(final MMIMolecule molecule) {
    final var substance = new Substance();

    // id
    substance.setId(DigestUtils.sha256Hex("Substance" + molecule.getId()));

    // meta
    substance.setMeta(
        new Meta()
            .addProfile(
                "http://hl7.org/fhir/StructureDefinition/Substance|4.0.1")
            .setSource("https://www.mmi.de/mmi-pharmindex"));

    // identifier
    substance.addIdentifier()
        .setSystem("https://www.mmi.de/mmi-pharmindex/molecule")
        .setValue(String.valueOf(molecule.getId()));

    // code
    substance.setCode(new CodeableConcept().setText(molecule.getName_plain()));

    // description
    substance.setDescription(molecule.getName());


    return substance;
  }
}
