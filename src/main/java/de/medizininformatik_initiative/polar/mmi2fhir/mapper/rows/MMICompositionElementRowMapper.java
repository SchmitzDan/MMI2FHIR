package de.medizininformatik_initiative.polar.mmi2fhir.mapper.rows;

import de.medizininformatik_initiative.polar.mmi2fhir.model.MMICompositionElement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class MMICompositionElementRowMapper implements RowMapper<MMICompositionElement> {

  @Override
  public MMICompositionElement mapRow(final ResultSet rs, final int rowNum) throws SQLException {
    return new MMICompositionElement(
        rs.getInt("ID"),
        rs.getInt("MOLECULEID"),
        RowMapperUtils.toInteger(rs.getString("MOLECULETYPECATALOGID")),
        rs.getString("MOLECULETYPECODE"),
        RowMapperUtils.toDouble(rs.getString("MASSFROM")),
        RowMapperUtils.toDouble(rs.getString("MASSTO")),
        RowMapperUtils.toInteger(rs.getString("MOLECULEUNITCATALOGID")),
        rs.getString("MOLECULEUNITCODE"));
  }

}
