package de.medizininformatik_initiative.polar.mmi2fhir.mapper.rows;

import de.medizininformatik_initiative.polar.mmi2fhir.model.MMIPackage;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class MMIPackageRowMapper implements RowMapper<MMIPackage> {

  @Override
  public MMIPackage mapRow(final ResultSet rs, final int rowNum) throws SQLException {

    return new MMIPackage(
        rs.getInt("ID"),
        rs.getInt("PRODUCTID"),
        rs.getString("PZN"),
        rs.getString("PRESCRIPTION_TEXT"),
        rs.getString("NAME"),
        rs.getString("PACKAGESORT"),
        RowMapperUtils.toInteger(rs.getString("SALESSTATUSCATALOGID")),
        rs.getString("SALESSTATUSCODE"),
        RowMapperUtils.toLocalDate(rs.getString("ONMARKETDATE")),
        rs.getString("PZNORIGINAL"),
        rs.getString("PZNSUCCESSOR"),
        RowMapperUtils.toInteger(rs.getString("PACKAGENORMSIZECATALOGID")),
        rs.getString("PACKAGENORMSIZECODE"),
        RowMapperUtils.toDouble(rs.getString("FIXEDPRICE")),
        RowMapperUtils.toDouble(rs.getString("PRICE")),
        RowMapperUtils.toDouble(rs.getString("RECOMMENDEDPRICE")),
        RowMapperUtils.toDouble(rs.getString("PHARMACYBUYPRICE")),
        RowMapperUtils.toDouble(rs.getString("PATIENTPAYMENT")),
        rs.getString("IFANAME"),
        RowMapperUtils.toInteger(rs.getString("IFAPHARMFORMCATALOGID")),
        rs.getString("IFAPHARMFORMCODE"),
        rs.getString("AMOUNTTEXT"),
        RowMapperUtils.toDouble(rs.getString("AMOUNT")),
        rs.getInt("FACTOR1"),
        rs.getInt("FACTOR2"),
        RowMapperUtils.toInteger(rs.getString("PACKAGEUNITCATALOGID")),
        rs.getString("PACKAGEUNITCODE"),
        RowMapperUtils.toInteger(rs.getString("PACKAGEPRICECOMPARISONGROUPID")),
        RowMapperUtils.toInteger(rs.getString("PACKAGEPRICECOMPARISONGROUPID2")),
        RowMapperUtils.toDouble(rs.getString("DISCOUNT130B")),
        rs.getString("PATIENTPAYMENTHINT"));
  }

}
