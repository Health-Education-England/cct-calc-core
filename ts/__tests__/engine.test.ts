import { readFileSync } from "node:fs";
import { resolve } from "node:path";
import { describe, expect, it } from "vitest";
import {
  calculateAbsenceExtensionDays,
  calculateInclusiveDaySpan,
  calculateLtftExtensionDays,
  calculateNewCct
} from "../src/engine";

const fixtures = JSON.parse(
  readFileSync(
    resolve(__dirname, "../../fixtures/calculation-test-cases.json"),
    "utf-8"
  )
);

describe("calculateInclusiveDaySpan (shared fixtures)", () => {
  for (const tc of fixtures.calculateInclusiveDaySpan) {
    if (tc.expectedError) {
      it(tc.name, () => {
        expect(() =>
          calculateInclusiveDaySpan(tc.input.startDate, tc.input.endDate)
        ).toThrow(tc.expectedError);
      });
    } else {
      it(tc.name, () => {
        const result = calculateInclusiveDaySpan(
          tc.input.startDate,
          tc.input.endDate
        );
        expect(result).toBe(tc.expected);
      });
    }
  }
});

describe("calculateAbsenceExtensionDays (shared fixtures)", () => {
  for (const tc of fixtures.calculateAbsenceExtensionDays) {
    if (tc.expectedError) {
      it(tc.name, () => {
        expect(() =>
          calculateAbsenceExtensionDays(
            tc.input.fullTimeDays,
            tc.input.trainingDaysRebate
          )
        ).toThrow(tc.expectedError);
      });
    } else {
      it(tc.name, () => {
        const result = calculateAbsenceExtensionDays(
          tc.input.fullTimeDays,
          tc.input.trainingDaysRebate
        );
        expect(result).toBe(tc.expected);
      });
    }
  }
});

describe("calculateLtftExtensionDays (shared fixtures)", () => {
  for (const tc of fixtures.calculateLtftExtensionDays) {
    if (tc.expectedError) {
      it(tc.name, () => {
        expect(() =>
          calculateLtftExtensionDays(
            tc.input.fullTimeDays,
            tc.input.ltftWte,
            tc.input.currentWte
          )
        ).toThrow(tc.expectedError);
      });
    } else {
      it(tc.name, () => {
        const result = calculateLtftExtensionDays(
          tc.input.fullTimeDays,
          tc.input.ltftWte,
          tc.input.currentWte
        );
        expect(result).toBe(tc.expected);
      });
    }
  }
});

describe("calculateNewCct (shared fixtures)", () => {
  for (const tc of fixtures.calculateNewCct) {
    if (tc.expectedError) {
      it(tc.name, () => {
        expect(() =>
          calculateNewCct(tc.input.baseDate, tc.input.extensionDays)
        ).toThrow(tc.expectedError);
      });
    } else {
      it(tc.name, () => {
        const result = calculateNewCct(
          tc.input.baseDate,
          tc.input.extensionDays
        );
        expect(result).toBe(tc.expected);
      });
    }
  }
});
