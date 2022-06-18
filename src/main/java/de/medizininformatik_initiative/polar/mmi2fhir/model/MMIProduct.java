package de.medizininformatik_initiative.polar.mmi2fhir.model;

import java.time.LocalDate;
import lombok.Data;

@Data
public class MMIProduct {
  private final int id;
  private final String name;
  private final String name_plain;
  private final String sortname;
  private final LocalDate onmarketdate;
  private final String registration_number;
  private final Integer activesubstancecount;
  private final Integer originalproductid;
  private final Integer dispensingtypecatalogid;
  private final String dispensingtypecode;
  private final Integer productfoodtypecatalogid;
  private final String productfoodtypecode;
  private final Integer productdieteticstypecatalogid;
  private final String productdieteticstypecode;
  private final Integer pao_timeunitcatalogid;
  private final String pao_timeunitcode;
  private final String pao_description;
  private final Integer pao_count;
  private final Boolean diga_flag;
}
