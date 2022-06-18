package de.medizininformatik_initiative.polar.mmi2fhir.reader;

import de.medizininformatik_initiative.polar.mmi2fhir.mapper.rows.MMIMoleculeRowMapper;
import de.medizininformatik_initiative.polar.mmi2fhir.model.MMIMolecule;
import javax.sql.DataSource;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MMIActiveIngredientReader extends JdbcCursorItemReader<MMIMolecule> {

  private static final String QUERY = """
      SELECT m.*
      FROM
        COMPOSITIONELEMENT ce
        JOIN MOLECULE m ON ce.MOLECULEID = m.ID
      WHERE
        ce.MOLECULETYPECODE = 'A'
      """;

  public MMIActiveIngredientReader(@Qualifier("mmiDataSource") final DataSource dataSource,
      final MMIMoleculeRowMapper rowMapper) {
    super();
    super.setName("ProductReader");
    super.setSql(QUERY);
    super.setRowMapper(rowMapper);
    super.setDataSource(dataSource);
  }


}
