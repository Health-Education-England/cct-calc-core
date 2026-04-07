import type calculationRules from "../../rules/calculation-rules.json";

export type CalculationType = keyof typeof calculationRules.types;

export type CalculationTypeConfig = {
  label: string;
  category: string;
  hasWteChangeField: boolean;
};

export type CalculationRules = {
  types: Record<string, CalculationTypeConfig>;
};
