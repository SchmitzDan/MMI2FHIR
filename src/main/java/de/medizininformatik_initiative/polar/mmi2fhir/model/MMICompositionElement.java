package de.medizininformatik_initiative.polar.mmi2fhir.model;

import lombok.Data;

@Data
public class MMICompositionElement {

  private final int id;
  private final int moleculeid;
  private final Integer moleculetypecatalogid;
  private final String moleculetypecode;
  private final Double massfrom;
  private final Double massto;
  private final Integer moleculeunitcatalogid;
  private final String moleculeunitcode;
}
