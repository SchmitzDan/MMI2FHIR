package de.medizininformatik_initiative.polar.mmi2fhir.model;

import java.time.LocalDate;
import lombok.Data;

@Data
public class MMIPackage {

  private final int id;
  private final int productId;
  private final String pzn;
  private final String prescriptionText;
  private final String name;
  private final String packageSort;
  private final Integer salesstatuscatalogid;
  private final String salesstatuscode;
  private final LocalDate onmarketdate;
  private final String pznoriginal;
  private final String pznsuccessor;
  private final Integer packagenormsizecatalogid;
  private final String packagenormsizecode;
  private final Double fixedprice;
  private final Double price;
  private final Double recommendedprice;
  private final Double pharmacybuyprice;
  private final Double patientpayment;
  private final String ifaname;
  private final Integer ifapharmformcatalogid;
  private final String ifapharmformcode;
  private final String amounttext;
  private final double amount;
  private final int factor1;
  private final int factor2;
  private final Integer packageunitcatalogid;
  private final String packageunitcode;
  private final Integer packagepricecomparisongroupid;
  private final Integer packagepricecomparisongroupid2;
  private final Double discount130b;
  private final String patientpaymenthint;
}
