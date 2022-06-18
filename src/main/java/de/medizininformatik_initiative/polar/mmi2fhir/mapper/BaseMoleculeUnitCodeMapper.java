package de.medizininformatik_initiative.polar.mmi2fhir.mapper;

import org.springframework.stereotype.Component;

@Component
public class BaseMoleculeUnitCodeMapper {

  // code, unit, system
  // CM2 cm² -> cm2, SquareCentiMeter

  // MCL µl Mikroliter -> uL, MicroLiter
  // ML ml -> mL MilliLiter
  // G g -> g Gram
  // MGMW mg (MW) -> mg, Milligram (TODO, validate!)
  // L l -> L Liter
  // MG mg -> mg, MilliGram
  // 527 "1 Hub Lösung zur Inhalation" ???
  // SPRUEHSTOSS ???

}
