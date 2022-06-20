package de.medizininformatik_initiative.polar.mmi2fhir.reader;

import de.medizininformatik_initiative.polar.mmi2fhir.mapper.rows.MMIProductRowMapper;
import de.medizininformatik_initiative.polar.mmi2fhir.model.MMIProduct;
import javax.sql.DataSource;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MMIProductReader extends JdbcCursorItemReader<MMIProduct> {

  private static final String QUERY = """
      SELECT *
      FROM PRODUCT;
      """;

  public MMIProductReader(@Qualifier("mmiDataSource") final DataSource dataSource,
      final MMIProductRowMapper rowMapper) {
    super();
    super.setName("ProductReader");
    super.setSql(QUERY);
    super.setRowMapper(rowMapper);
    super.setDataSource(dataSource);
  }


}
