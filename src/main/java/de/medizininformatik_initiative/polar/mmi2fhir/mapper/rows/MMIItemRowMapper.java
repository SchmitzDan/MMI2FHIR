package de.medizininformatik_initiative.polar.mmi2fhir.mapper.rows;

import de.medizininformatik_initiative.polar.mmi2fhir.model.MMIItem;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class MMIItemRowMapper implements RowMapper<MMIItem> {

  @Override
  public MMIItem mapRow(final ResultSet rs, final int rowNum) throws SQLException {
    return new MMIItem(
        rs.getInt("ID"),
        rs.getString("NAME"),
        rs.getInt("PRODUCTID"),
        rs.getString("SORTNAME"),
        RowMapperUtils.toDouble(rs.getString("BASECOUNT")),
        RowMapperUtils.toInteger(rs.getString("BASEMOLECULEUNITCATALOGID")),
        rs.getString("BASEMOLECULEUNITCODE"),
        RowMapperUtils.toInteger(rs.getString("PHARMFORMCATALOGID")),
        rs.getString("PHARMFORMCODE"),
        RowMapperUtils.toDouble(rs.getString("USEFULLHEAT")),
        RowMapperUtils.toInteger(rs.getString("ITEMROACATALOGID")),
        rs.getString("ITEMROACODE"),
        RowMapperUtils.toInteger(rs.getString("ITEMAPPLICATIONPLACECATALOGID")),
        rs.getString("ITEMAPPLICATIONPLACECODE"),
        RowMapperUtils.toInteger(rs.getString("ITEMSUPPLIESTYPECATALOGID")),
        rs.getString("ITEMSUPPLIESTYPECODE"),
        RowMapperUtils.toInteger(rs.getString("ITEMRELEASETYPECATALOGID")),
        rs.getString("ITEMRELEASETYPECODE"),
        RowMapperUtils.toDouble(rs.getString("MAINBASECOUNT")),
        RowMapperUtils.toInteger(rs.getString("MAINBASEMOLECULEUNITCATALOGID")),
        rs.getString("MAINBASEMOLECULEUNITCODE"));
  }

}
