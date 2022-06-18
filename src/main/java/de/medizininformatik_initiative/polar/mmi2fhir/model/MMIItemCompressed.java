package de.medizininformatik_initiative.polar.mmi2fhir.model;

import java.util.List;
import lombok.Data;

@Data
public class MMIItemCompressed {
  private final List<Integer> ids;
  private final String name;
  private final List<Integer> productids;
  private final String sortname;
  private final Double basecount;
  private final Integer basemoleculeunitcatalogid;
  private final String basemoleculeunitcode;
  private final Integer pharmformcatalogid;
  private final String pharmformcode;
  private final Double usefullheat;
  private final Integer itemroacatalogid;
  private final String itemroacode;
  private final Integer itemapplicationplacecatalogid;
  private final String itemapplicationplacecode;
  private final Integer itemsuppliestypecatalogid;
  private final String itemsuppliestypecode;
  private final Integer itemreleasetypecatalogid;
  private final String itemreleasetypecode;
  private final Double mainbasecount;
  private final Integer mainbasemoleculeunitcatalogid;
  private final String mainbasemoleculeunitcode;
}
