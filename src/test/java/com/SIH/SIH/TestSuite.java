package com.SIH.SIH;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

/**
 * Comprehensive Test Suite for SIH Complaint Management System
 * 
 * This test suite runs all tests including:
 * - Unit Tests (Controllers, Services, Utils)
 * - Integration Tests (End-to-end scenarios)
 * - Builder Tests (Test data creation)
 * 
 * Run this class to execute all tests in the project.
 */
@Suite
@SelectPackages({
    "com.SIH.SIH.controller",
    "com.SIH.SIH.services", 
    "com.SIH.SIH.util",
    "com.SIH.SIH.integration",
    "com.SIH.SIH.builder"
})
public class TestSuite {
    // This class serves as a test suite runner
    // All tests in the specified packages will be executed
}
