package uk.nhs.tis.cctcalccore;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CctCalculationEngineTest {

    private static JsonNode fixtures;

    @BeforeAll
    static void setUp() throws Exception {
        try (InputStream is = CctCalculationEngineTest.class.getClassLoader()
                .getResourceAsStream("calculation-test-cases.json")) {
            fixtures = new ObjectMapper().readTree(is);
        }
    }

    @TestFactory
    Collection<DynamicTest> calculateInclusiveDaySpanTests() {
        Collection<DynamicTest> tests = new ArrayList<>();

        for (JsonNode tc : fixtures.get("calculateInclusiveDaySpan")) {
            String name = tc.get("name").asText();
            JsonNode input = tc.get("input");
            if (tc.has("expectedError")) {
                String expectedError = tc.get("expectedError").asText();
                tests.add(DynamicTest.dynamicTest(name, () -> {
                    IllegalArgumentException ex = assertThrows(
                            IllegalArgumentException.class,
                            () -> CctCalculationEngine.calculateInclusiveDaySpan(
                                    input.get("startDate").asText(),
                                    input.get("endDate").asText()
                            )
                    );
                    assertTrue(ex.getMessage().contains(expectedError));
                }));
            } else {
                int expected = tc.get("expected").asInt();
                tests.add(DynamicTest.dynamicTest(name, () -> {
                    int result = CctCalculationEngine.calculateInclusiveDaySpan(
                            input.get("startDate").asText(),
                            input.get("endDate").asText()
                    );
                    assertEquals(expected, result);
                }));
            }
        }

        return tests;
    }

    @TestFactory
    Collection<DynamicTest> calculateAbsenceExtensionDaysTests() {
        Collection<DynamicTest> tests = new ArrayList<>();

        for (JsonNode tc : fixtures.get("calculateAbsenceExtensionDays")) {
            String name = tc.get("name").asText();
            JsonNode input = tc.get("input");

            if (tc.has("expectedError")) {
                String expectedError = tc.get("expectedError").asText();
                tests.add(DynamicTest.dynamicTest(name, () -> {
                    IllegalArgumentException ex = assertThrows(
                            IllegalArgumentException.class,
                            () -> CctCalculationEngine.calculateAbsenceExtensionDays(
                                    input.get("fullTimeDays").asInt(),
                                    input.has("trainingDaysRebate")
                                            ? input.get("trainingDaysRebate").asDouble()
                                            : 0
                            )
                    );
                    assertTrue(ex.getMessage().contains(expectedError));
                }));
            } else {
                int expected = tc.get("expected").asInt();
                tests.add(DynamicTest.dynamicTest(name, () -> {
                    int result = input.has("trainingDaysRebate")
                            ? CctCalculationEngine.calculateAbsenceExtensionDays(
                                    input.get("fullTimeDays").asInt(),
                                    input.get("trainingDaysRebate").asDouble()
                            )
                            : CctCalculationEngine.calculateAbsenceExtensionDays(
                                    input.get("fullTimeDays").asInt()
                            );
                    assertEquals(expected, result);
                }));
            }
        }

        return tests;
    }

    @TestFactory
    Collection<DynamicTest> calculateLtftExtensionDaysTests() {
        Collection<DynamicTest> tests = new ArrayList<>();

        for (JsonNode tc : fixtures.get("calculateLtftExtensionDays")) {
            String name = tc.get("name").asText();
            JsonNode input = tc.get("input");

            if (tc.has("expectedError")) {
                String expectedError = tc.get("expectedError").asText();
                tests.add(DynamicTest.dynamicTest(name, () -> {
                    IllegalArgumentException ex = assertThrows(
                            IllegalArgumentException.class,
                        () -> {
                        if (input.has("currentWte")) {
                            CctCalculationEngine.calculateLtftExtensionDays(
                                input.get("fullTimeDays").asInt(),
                                input.get("ltftWte").asDouble(),
                                input.get("currentWte").asDouble()
                            );
                        } else {
                            CctCalculationEngine.calculateLtftExtensionDays(
                                input.get("fullTimeDays").asInt(),
                                input.get("ltftWte").asDouble()
                            );
                        }
                        }
                    );
                    assertTrue(ex.getMessage().contains(expectedError));
                }));
            } else {
                int expected = tc.get("expected").asInt();
                tests.add(DynamicTest.dynamicTest(name, () -> {
                    int result = input.has("currentWte")
                            ? CctCalculationEngine.calculateLtftExtensionDays(
                                    input.get("fullTimeDays").asInt(),
                                    input.get("ltftWte").asDouble(),
                                    input.get("currentWte").asDouble()
                            )
                            : CctCalculationEngine.calculateLtftExtensionDays(
                                    input.get("fullTimeDays").asInt(),
                                    input.get("ltftWte").asDouble()
                            );
                    assertEquals(expected, result);
                }));
            }
        }

        return tests;
    }

    @TestFactory
    Collection<DynamicTest> calculateNewCctTests() {
        Collection<DynamicTest> tests = new ArrayList<>();

        for (JsonNode tc : fixtures.get("calculateNewCct")) {
            String name = tc.get("name").asText();
            JsonNode input = tc.get("input");
            if (tc.has("expectedError")) {
                String expectedError = tc.get("expectedError").asText();
                tests.add(DynamicTest.dynamicTest(name, () -> {
                    IllegalArgumentException ex = assertThrows(
                            IllegalArgumentException.class,
                            () -> CctCalculationEngine.calculateNewCct(
                                    input.get("baseDate").asText(),
                                    input.get("extensionDays").asInt()
                            )
                    );
                    assertTrue(ex.getMessage().contains(expectedError));
                }));
            } else {
                String expected = tc.get("expected").asText();
                tests.add(DynamicTest.dynamicTest(name, () -> {
                    String result = CctCalculationEngine.calculateNewCct(
                            input.get("baseDate").asText(),
                            input.get("extensionDays").asInt()
                    );
                    assertEquals(expected, result);
                }));
            }
        }

        return tests;
    }
}
