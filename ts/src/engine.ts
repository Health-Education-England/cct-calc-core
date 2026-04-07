import dayjs from "dayjs";
import rules from "../../rules/calculation-rules.json";
import type { CalculationRules } from "./types";

export const calculationRules: CalculationRules = rules as CalculationRules;

export const calculateInclusiveDaySpan = (
  startDate: string,
  endDate: string
): number => {
  const start = dayjs(startDate);
  const end = dayjs(endDate);
  if (!start.isValid()) {
    throw new RangeError(`Invalid startDate: ${startDate}.`);
  }
  if (!end.isValid()) {
    throw new RangeError(`Invalid endDate: ${endDate}.`);
  }
  if (end.isBefore(start)) {
    throw new RangeError(
      `endDate (${endDate}) must not be before startDate (${startDate}).`
    );
  }
  return end.diff(start, "days") + 1;
};

export const calculateAbsenceExtensionDays = (
  fullTimeDays: number,
  trainingDaysRebate: number = 0
): number => {
  if (fullTimeDays <= 0) {
    throw new RangeError(`Invalid fullTimeDays: ${fullTimeDays}. Must be > 0.`);
  }
  if (trainingDaysRebate < 0 || trainingDaysRebate > fullTimeDays) {
    throw new RangeError(
      `Invalid training days rebate: ${trainingDaysRebate}. Must be between 0 and fullTimeDays (${fullTimeDays}).`
    );
  }
  return fullTimeDays - Math.round(trainingDaysRebate);
};

export const calculateLtftExtensionDays = (
  fullTimeDays: number,
  ltftWte: number,
  currentWte: number = 1
): number => {
  if (fullTimeDays <= 0) {
    throw new RangeError(`Invalid fullTimeDays: ${fullTimeDays}. Must be > 0.`);
  }
  if (ltftWte < 0.1 || ltftWte > 1) {
    throw new RangeError(
      `Invalid ltftWte: ${ltftWte}. Must be between 0.1 and 1.`
    );
  }
  if (currentWte < 0.1 || currentWte > 1) {
    throw new RangeError(
      `Invalid currentWte: ${currentWte}. Must be between 0.1 and 1.`
    );
  }
  const wteDays = fullTimeDays * (ltftWte / currentWte);
  return Math.round(fullTimeDays - wteDays);
};

export const calculateNewCct = (
  baseDate: string,
  extensionDays: number
): string => {
  const base = dayjs(baseDate);
  if (!base.isValid()) {
    throw new RangeError(`Invalid baseDate: ${baseDate}.`);
  }
  return base.add(extensionDays, "day").format("YYYY-MM-DD");
};
