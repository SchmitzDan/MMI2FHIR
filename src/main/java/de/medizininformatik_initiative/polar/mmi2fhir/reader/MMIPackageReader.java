package de.medizininformatik_initiative.polar.mmi2fhir.reader;

import de.medizininformatik_initiative.polar.mmi2fhir.mapper.rows.MMIPackageRowMapper;
import de.medizininformatik_initiative.polar.mmi2fhir.model.MMIPackage;
import javax.sql.DataSource;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MMIPackageReader extends JdbcCursorItemReader<MMIPackage> {

  private static final String QUERY = """
      SELECT *
      FROM PACKAGE;
      """;

  public MMIPackageReader(@Qualifier("mmiDataSource") final DataSource dataSource,
      final MMIPackageRowMapper rowMapper) {
    super();
    super.setName("PackageReader");
    super.setSql(QUERY);
    super.setRowMapper(rowMapper);
    super.setDataSource(dataSource);
  }


}
