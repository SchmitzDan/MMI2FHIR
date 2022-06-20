package de.medizininformatik_initiative.polar.mmi2fhir.mapper.rows;

import de.medizininformatik_initiative.polar.mmi2fhir.model.MMIItemCompressed;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class MMIItemCompressedRowMapper implements RowMapper<MMIItemCompressed> {

  @Override
  public MMIItemCompressed mapRow(final ResultSet rs, final int rowNum) throws SQLException {
    return new MMIItemCompressed(
        toIntegerList(rs.getArray("ids").getResultSet()),
        rs.getString("NAME"),
        toIntegerList(rs.getArray("products").getResultSet()),
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

  private List<Integer> toIntegerList(final ResultSet rs) throws SQLException {
    final var result = new ArrayList<Integer>();
    while (rs.next()) {
      result.add(rs.getInt(2));
    }
    return result;
  }

}
