package de.medizininformatik_initiative.polar.mmi2fhir.mapper.rows;

import de.medizininformatik_initiative.polar.mmi2fhir.model.MMIMolecule;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class MMIMoleculeRowMapper implements RowMapper<MMIMolecule> {

  @Override
  public MMIMolecule mapRow(final ResultSet rs, final int rowNum) throws SQLException {
    return new MMIMolecule(
        rs.getInt("ID"),
        rs.getString("NAME"),
        rs.getString("NAME_PLAIN"),
        rs.getString("SORTNAME"),
        rs.getString("CHEMICALNAME"),
        RowMapperUtils.toInteger(rs.getString("UPPERMOLECULEID")),
        RowMapperUtils.toInteger(rs.getString("MOLECULENATURECATALOGID")),
        rs.getString("MOLECULENATURECODE"),
        RowMapperUtils.toDouble(rs.getString("MOLWEIGHT")),
        RowMapperUtils.toDouble(rs.getString("IONICSTRENGTH")),
        RowMapperUtils.toDouble(rs.getString("Q0VALUE")),
        RowMapperUtils.toDouble(rs.getString("HALFLIFE")),
        RowMapperUtils.toInteger(rs.getString("HALFLIFETIMEUNITCATALOGID")),
        rs.getString("HALFLIFETIMEUNITCODE"),
        rs.getBoolean("DOPING_FLAG"),
        RowMapperUtils.toInteger(rs.getString("DOPINGDESCCATALOGID")),
        rs.getString("DOPINGDESCCODE"),
        RowMapperUtils.toBoolean(rs.getString("ANESTHETIC_FLAG")),
        rs.getString("ASKNUMBER"),
        rs.getString("CASREGISTRATIONNUMBER"));
  }

}
