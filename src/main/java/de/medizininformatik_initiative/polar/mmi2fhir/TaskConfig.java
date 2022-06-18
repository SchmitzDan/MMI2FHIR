package de.medizininformatik_initiative.polar.mmi2fhir;

import de.medizininformatik_initiative.polar.mmi2fhir.model.MMIItemCompressed;
import de.medizininformatik_initiative.polar.mmi2fhir.model.MMIMolecule;
import de.medizininformatik_initiative.polar.mmi2fhir.model.MMIPackage;
import de.medizininformatik_initiative.polar.mmi2fhir.model.MMIProduct;
import de.medizininformatik_initiative.polar.mmi2fhir.processors.ActiveIngredientProcessor;
import de.medizininformatik_initiative.polar.mmi2fhir.processors.MMIItemCompressedProcessor;
import de.medizininformatik_initiative.polar.mmi2fhir.processors.MoleculeProcessor;
import de.medizininformatik_initiative.polar.mmi2fhir.processors.PackageProcessor;
import de.medizininformatik_initiative.polar.mmi2fhir.processors.ProductProcessor;
import de.medizininformatik_initiative.polar.mmi2fhir.reader.MMIActiveIngredientReader;
import de.medizininformatik_initiative.polar.mmi2fhir.reader.MMIItemCompressedReader;
import de.medizininformatik_initiative.polar.mmi2fhir.reader.MMIMoleculesWithoutCodeReader;
import de.medizininformatik_initiative.polar.mmi2fhir.reader.MMIPackageReader;
import de.medizininformatik_initiative.polar.mmi2fhir.reader.MMIProductReader;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class TaskConfig {

  @Autowired
  private JobBuilderFactory jobBuilderFactory;

  @Autowired
  private StepBuilderFactory stepBuilderFactory;

  @Value("${batch.chunkSize}")
  private int chunkSize;

  @Value("${data.writeBundlesToFile}")
  private boolean shouldWriteBundlesToFile;

  @Autowired
  private ItemWriter<BundleEntryComponent> writer;

  @Bean
  public Step activeIngredientsStep(final MMIActiveIngredientReader reader,
      final ActiveIngredientProcessor processor) {
    return stepBuilderFactory.get("Active ingredients")
        .<MMIMolecule, BundleEntryComponent>chunk(chunkSize)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .build();
  }

  @Bean
  public Job job() {
    return jobBuilderFactory.get("MMI-2-FHIR")
        .incrementer(new RunIdIncrementer())
        .start(moleculesStep(null, null))
        .next(activeIngredientsStep(null, null))
        .next(mmiItemsCompressedStep(null, null))
        .next(productStep(null, null))
        .next(packageStep(null, null))
        .build();
  }

  @Bean
  public Step mmiItemsCompressedStep(final MMIItemCompressedReader reader,
      final MMIItemCompressedProcessor processor) {
    return stepBuilderFactory.get("Compressed Items")
        .<MMIItemCompressed, BundleEntryComponent>chunk(chunkSize)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .build();
  }

  @Bean
  public Step moleculesStep(final MMIMoleculesWithoutCodeReader reader,
      final MoleculeProcessor processor) {
    return stepBuilderFactory.get("Molecules without code")
        .<MMIMolecule, BundleEntryComponent>chunk(chunkSize)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .build();
  }

  @Bean
  public Step packageStep(final MMIPackageReader reader,
      final PackageProcessor processor) {
    return stepBuilderFactory.get("Packages")
        .<MMIPackage, BundleEntryComponent>chunk(chunkSize)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .build();
  }

  @Bean
  public Step productStep(final MMIProductReader reader,
      final ProductProcessor processor) {
    return stepBuilderFactory.get("Products")
        .<MMIProduct, BundleEntryComponent>chunk(chunkSize)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .build();
  }

}
