package com.github.andreptb.fitnesse;

import org.junit.runner.RunWith;

import fitnesse.junit.FitNesseRunner;

/**
 * Jdbc Fixture unit testing
 */
/**
 * Slim Fixture testing. Configured to run FitNesseSeleniumSlim.SeleniumFixtureTests suite.
 */
@RunWith(FitNesseRunner.class)
@FitNesseRunner.Suite(".FitNesseJdbcSlim.JdbcFixtureTests")
@FitNesseRunner.FitnesseDir("fitnesse")
@FitNesseRunner.OutputDir("target/fitnesse")
public class JdbcFixtureTest {

}
