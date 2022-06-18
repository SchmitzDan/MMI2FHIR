package de.medizininformatik_initiative.polar.mmi2fhir.dao;

import de.medizininformatik_initiative.polar.mmi2fhir.model.Atc;
import de.medizininformatik_initiative.polar.mmi2fhir.model.MMICompositionElement;
import de.medizininformatik_initiative.polar.mmi2fhir.model.MMIItem;
import de.medizininformatik_initiative.polar.mmi2fhir.model.MMIMolecule;
import java.util.List;

public interface MMIDao {

  List<Atc> loadAtcsForItem(int itemId);

  List<Atc> loadAtcsForMolecule(int moleculeId);

  List<MMICompositionElement> loadCompositionElementsForItem(int itemId);

  List<MMIItem> loadItemsForProduct(int productId);

  MMIMolecule loadMolecule(int moleculeId);
}
