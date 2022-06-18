package de.medizininformatik_initiative.polar.mmi2fhir.mapper;

import de.medizininformatik_initiative.polar.mmi2fhir.model.MMIPackage;
import org.apache.commons.codec.digest.DigestUtils;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Ratio;
import org.hl7.fhir.r4.model.Reference;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class PackageMapper {

  public Medication map(final MMIPackage mmiPackage) {

    final var medication = new Medication();

    // id
    medication.setId(DigestUtils.sha256Hex("MMIPackage" + mmiPackage.getId()));

    // meta
    medication.setMeta(
        new Meta()
            .addProfile(
                "https://www.medizininformatik-initiative.de/fhir/core/modul-medikation/StructureDefinition/Medication")
            .setSource("https://www.mmi.de/mmi-pharmindex"));

    // identifier
    medication.addIdentifier()
        .setSystem("https://www.mmi.de/mmi-pharmindex/package")
        .setValue(String.valueOf(mmiPackage.getId()));

    // code
    medication.setCode(
        new CodeableConcept()
            .setText(mmiPackage.getPrescriptionText()));

    if (StringUtils.hasText(mmiPackage.getPzn())) {
      medication.getCode().addCoding(
          new Coding(
              "http://fhir.de/CodeSystem/ifa/pzn",
              mmiPackage.getPzn(),
              mmiPackage.getIfaname()));
    }

    // amount
    // TODO!!!

    // ingredient
    medication.addIngredient()
        .setItem(new Reference()
            .setType("Medication")
            .setIdentifier(
                new Identifier()
                    .setSystem("https://www.mmi.de/mmi-pharmindex/product")
                    .setValue(String.valueOf(mmiPackage.getProductId()))))
        .setStrength(new Ratio()
            .setNumerator(new Quantity()
                .setValue(
                    mmiPackage.getAmount() * mmiPackage.getFactor1() * mmiPackage.getFactor2()) // TODO:
                                                                                                // korrekt?
                                                                                                // Or
                                                                                                // better
                                                                                                // always
                                                                                                // "piece"
                                                                                                // of
                                                                                                // product
                                                                                                // and
                                                                                                // amount
                                                                                                // in
                                                                                                // field
                                                                                                // amount?
                .setCode(mmiPackage.getPackageunitcode())
                .setSystem(mmiPackage.getPackageunitcatalogid() == null ? null
                    : "https://www.mmi.de/mmi-pharmindex/catalog/"
                        + mmiPackage.getPackageunitcatalogid()))
            .setDenominator(new Quantity()
                .setValue(1)));

    return medication;
  }
}
