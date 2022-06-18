package de.medizininformatik_initiative.polar.mmi2fhir.dao;

import de.medizininformatik_initiative.polar.mmi2fhir.mapper.rows.AtcRowMapper;
import de.medizininformatik_initiative.polar.mmi2fhir.mapper.rows.MMICompositionElementRowMapper;
import de.medizininformatik_initiative.polar.mmi2fhir.mapper.rows.MMIItemRowMapper;
import de.medizininformatik_initiative.polar.mmi2fhir.mapper.rows.MMIMoleculeRowMapper;
import de.medizininformatik_initiative.polar.mmi2fhir.model.Atc;
import de.medizininformatik_initiative.polar.mmi2fhir.model.MMICompositionElement;
import de.medizininformatik_initiative.polar.mmi2fhir.model.MMIItem;
import de.medizininformatik_initiative.polar.mmi2fhir.model.MMIMolecule;
import java.sql.Types;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class MMIDaoImpl implements MMIDao {

  private static final String ITEMS_BY_PRODUCT_ID_QUERY = """
      SELECT *
      FROM ITEM
      WHERE PRODUCTID = ?
      """;

  private static final String COMPOSITION_ELEMENTS_BY_ITEM_ID_QUERY = """
      SELECT c.*
      FROM COMPOSITIONELEMENT c
        JOIN ITEM_COMPOSITIONELEMENT ic ON c.ID = ic.COMPOSITIONELEMENTID
      WHERE ic.ITEMID = ?
      """;

  private static final String MOLECULE_BY_ID_QUERY = """
      SELECT *
      FROM MOLECULE
      WHERE ID = ?
      """;

  private static final String ATCS_BY_ITEM_ID_QUERY = """
      SELECT DISTINCT
        c.CODE,
        c.DESCRIPTION
      FROM ITEM_ATC ia
        JOIN CATALOGENTRY c ON ia.ATCCATALOGID = c.CATALOGID AND ia.ATCCODE = c.CODE
      WHERE ia.ITEMID = ?
      """;

  private static final String ATCS_BY_MOLECULE_ID_QUERY = """
      SELECT DISTINCT
        c.CODE,
        c.DESCRIPTION
      FROM
        COMPOSITIONELEMENT ce
        JOIN ITEM_COMPOSITIONELEMENT ic ON ce.ID = ic.COMPOSITIONELEMENTID
        JOIN ITEM_ATC ia ON ic.ITEMID = ia.ITEMID
        JOIN CATALOGENTRY c ON ia.ATCCATALOGID = c.CATALOGID AND ia.ATCCODE = c.CODE
      WHERE
        ce.MOLECULETYPECODE = 'A'
        AND ce.MOLECULEID = ?
            """;

  private JdbcTemplate jdbcTemplate;

  @Autowired
  @Qualifier("mmiDataSource")
  private DataSource dataSource;

  @Autowired
  private MMIItemRowMapper itemRowMapper;

  @Autowired
  private MMICompositionElementRowMapper compositionElementRowMapper;

  @Autowired
  private MMIMoleculeRowMapper moleculeRowMapper;

  @Autowired
  private AtcRowMapper atcRowMapper;

  @PostConstruct
  private void afterPropertiesSet() {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public List<Atc> loadAtcsForItem(final int itemId) {
    log.trace("Load Atcs for item with id {}", itemId);
    return this.jdbcTemplate.query(
        ATCS_BY_ITEM_ID_QUERY,
        new Object[] {itemId},
        new int[] {Types.VARCHAR},
        atcRowMapper);
  }

  @Override
  public List<Atc> loadAtcsForMolecule(final int moleculeId) {
    log.trace("Load Atcs for molecule with id {}", moleculeId);
    return this.jdbcTemplate.query(
        ATCS_BY_MOLECULE_ID_QUERY,
        new Object[] {moleculeId},
        new int[] {Types.VARCHAR},
        atcRowMapper);
  }

  @Override
  public List<MMICompositionElement> loadCompositionElementsForItem(final int itemId) {
    log.trace("Load compositionElements for item with id {}", itemId);
    return this.jdbcTemplate.query(
        COMPOSITION_ELEMENTS_BY_ITEM_ID_QUERY,
        new Object[] {itemId},
        new int[] {Types.VARCHAR},
        compositionElementRowMapper);
  }

  @Override
  public List<MMIItem> loadItemsForProduct(final int productId) {
    log.trace("Load items for product with id {}", productId);
    return this.jdbcTemplate.query(
        ITEMS_BY_PRODUCT_ID_QUERY,
        new Object[] {productId},
        new int[] {Types.VARCHAR},
        itemRowMapper);
  }

  @Override
  public MMIMolecule loadMolecule(final int moleculeId) {
    log.trace("Load molecule with id {}", moleculeId);
    return this.jdbcTemplate.queryForObject(
        MOLECULE_BY_ID_QUERY,
        new Object[] {moleculeId},
        new int[] {Types.VARCHAR},
        moleculeRowMapper);
  }

}
