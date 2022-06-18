package de.medizininformatik_initiative.polar.mmi2fhir.model;

import lombok.Data;

@Data
public class MMIMolecule {
  private final int id;
  private final String name;
  private final String name_plain;
  private final String sortname;
  private final String chemicalname;
  private final Integer uppermoleculeid;
  private final Integer moleculenaturecatalogid;
  private final String moleculenaturecode;
  private final Double molweight;
  private final Double ionicstrength;
  private final Double q0value;
  private final Double halflife;
  private final Integer halflifetimeunitcatalogid;
  private final String halflifetimeunitcode;
  private final boolean doping_flag;
  private final Integer dopingdesccatalogid;
  private final String dopingdesccode;
  private final Boolean anesthetic_flag;
  private final String asknumber;
  private final String casregistrationnumber;
}
