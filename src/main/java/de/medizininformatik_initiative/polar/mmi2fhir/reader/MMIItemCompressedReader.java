package de.medizininformatik_initiative.polar.mmi2fhir.reader;

import de.medizininformatik_initiative.polar.mmi2fhir.mapper.rows.MMIItemCompressedRowMapper;
import de.medizininformatik_initiative.polar.mmi2fhir.model.MMIItemCompressed;
import javax.sql.DataSource;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MMIItemCompressedReader extends JdbcCursorItemReader<MMIItemCompressed> {

  private static final String QUERY =
      """
          with ceforitem as (
            select ic.ITEMID,
              listagg(distinct ic.COMPOSITIONELEMENTID) within group (order by ic.COMPOSITIONELEMENTID) as compositionElements
            from ITEM_COMPOSITIONELEMENT ic
            group by ic.ITEMID),

          atcForItem as (
            select ia.ITEMID,
              listagg(distinct ia.ATCCODE) within group (order by ia.ATCCODE) as atcs
            from ITEM_ATC ia
            group by ia.ITEMID
          )

          select
            compositionElements,
            atcs,
            array_agg(i.ID) as ids,
            array_agg(i.productid) as products,
            i.NAME,
            i.SORTNAME,
            i.BASECOUNT,
            i.BASEMOLECULEUNITCATALOGID,
            i.BASEMOLECULEUNITCODE,
            i.PHARMFORMCATALOGID,
            i.PHARMFORMCODE,
            i.USEFULLHEAT,
            i.ITEMROACATALOGID,
            i.ITEMROACODE,
            i.ITEMAPPLICATIONPLACECATALOGID,
            i.ITEMAPPLICATIONPLACECODE,
            i.ITEMSUPPLIESTYPECATALOGID,
            i.ITEMSUPPLIESTYPECODE,
            i.ITEMRELEASETYPECATALOGID,
            i.ITEMRELEASETYPECODE,
            i.MAINBASECOUNT,
            i.MAINBASEMOLECULEUNITCATALOGID,
            i.MAINBASEMOLECULEUNITCODE
          from ITEM i
            join ceForItem ce on i.ID = ce.ITEMID
            join atcForItem ai on i.ID = ai.ITEMID
          group by
            ce.compositionElements,
            ai.atcs,
            i.NAME,
            i.SORTNAME,
            i.BASECOUNT,
            i.BASEMOLECULEUNITCATALOGID,
            i.BASEMOLECULEUNITCODE,
            i.PHARMFORMCATALOGID,
            i.PHARMFORMCODE,
            i.USEFULLHEAT,
            i.ITEMROACATALOGID,
            i.ITEMROACODE,
            i.ITEMAPPLICATIONPLACECATALOGID,
            i.ITEMAPPLICATIONPLACECODE,
            i.ITEMSUPPLIESTYPECATALOGID,
            i.ITEMSUPPLIESTYPECODE,
            i.ITEMRELEASETYPECATALOGID,
            i.ITEMRELEASETYPECODE,
            i.MAINBASECOUNT,
            i.MAINBASEMOLECULEUNITCATALOGID,
            i.MAINBASEMOLECULEUNITCODE
                """;

  public MMIItemCompressedReader(@Qualifier("mmiDataSource") final DataSource dataSource,
      final MMIItemCompressedRowMapper rowMapper) {
    super();
    super.setName("CompressedItemReader");
    super.setSql(QUERY);
    super.setRowMapper(rowMapper);
    super.setDataSource(dataSource);
  }


}
