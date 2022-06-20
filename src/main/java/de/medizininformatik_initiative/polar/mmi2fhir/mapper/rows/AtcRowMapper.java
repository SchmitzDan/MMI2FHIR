package de.medizininformatik_initiative.polar.mmi2fhir.mapper.rows;

import de.medizininformatik_initiative.polar.mmi2fhir.model.Atc;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class AtcRowMapper implements RowMapper<Atc> {

  @Override
  public Atc mapRow(final ResultSet rs, final int rowNum) throws SQLException {
    return new Atc(
        rs.getString("CODE"),
        rs.getString("DESCRIPTION"));
  }

}
