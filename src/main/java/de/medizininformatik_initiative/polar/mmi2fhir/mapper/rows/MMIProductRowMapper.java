package de.medizininformatik_initiative.polar.mmi2fhir.mapper.rows;

import de.medizininformatik_initiative.polar.mmi2fhir.model.MMIProduct;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class MMIProductRowMapper implements RowMapper<MMIProduct> {

  @Override
  public MMIProduct mapRow(final ResultSet rs, final int rowNum) throws SQLException {
    return new MMIProduct(rs.getInt("ID"),
        rs.getString("NAME"),
        rs.getString("NAME_PLAIN"),
        rs.getString("SORTNAME"),
        RowMapperUtils.toLocalDate(rs.getString("ONMARKETDATE")),
        rs.getString("REGISTRATION_NUMBER"),
        RowMapperUtils.toInteger(rs.getString("ACTIVESUBSTANCECOUNT")),
        RowMapperUtils.toInteger(rs.getString("ORIGINALPRODUCTID")),
        RowMapperUtils.toInteger(rs.getString("DISPENSINGTYPECATALOGID")),
        rs.getString("DISPENSINGTYPECODE"),
        RowMapperUtils.toInteger(rs.getString("PRODUCTFOODTYPECATALOGID")),
        rs.getString("PRODUCTFOODTYPECODE"),
        RowMapperUtils.toInteger(rs.getString("PRODUCTDIETETICSTYPECATALOGID")),
        rs.getString("PRODUCTDIETETICSTYPECODE"),
        RowMapperUtils.toInteger(rs.getString("PAO_TIMEUNITCATALOGID")),
        rs.getString("PAO_TIMEUNITCODE"),
        rs.getString("PAO_DESCRIPTION"),
        RowMapperUtils.toInteger(rs.getString("PAO_COUNT")),
        RowMapperUtils.toBoolean(rs.getString("DIGA_FLAG")));
  }

}
