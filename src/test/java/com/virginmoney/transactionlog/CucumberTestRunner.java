package com.virginmoney.transactionlog;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@SuppressWarnings("NewClassNamingConvention")
@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/features"},
        glue = {"com.virginmoney.transactionlog.cucumberglue"})
public class CucumberTestRunner {

}
