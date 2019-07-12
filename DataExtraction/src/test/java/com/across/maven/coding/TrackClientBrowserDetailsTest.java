package com.across.maven.coding;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.Test;

import com.across.maven.coding.dto.CountryData;
import com.across.maven.coding.exception.InvalidNumberOfArgumentsException;
import com.across.maven.coding.utililty.StringUtil;
/**
 * Unit test for simple TrackClientBrowserDetails.
 */
public class TrackClientBrowserDetailsTest 
    
{
    static Predicate<CountryData> EU_COUNTRIES = countryData -> Arrays.asList("AT", "BE", "BG", "CY", "CZ", "DE", "DK", "EE", "ES", "FI", "FR", "GB",
                "GR", "HR", "HU", "IE", "IT", "LT", "LU", "LV", "MT", "NL", "PO", "PT", "RO", "SE", "SI", "SK").contains(countryData.getCountry());
    static Predicate<CountryData> IS_USER_AGENT_PRESENT = country -> StringUtil.isNullOrEmpty(country.getUserAgent());
    /**
     * Input arguments validation
     * @throws InvalidNumberOfArgumentsException
     */
    @Test(expected=InvalidNumberOfArgumentsException.class)
    public void testNumberOfArguments() throws InvalidNumberOfArgumentsException
    {
        TrackClientBrowserDetails.main(new String[] {"src/test/resources/ccds2.tsv"});
    }
    /**
     * Positive and Negative asserts on test case to prove correctness of the solution provided
     * @throws InvalidNumberOfArgumentsException
     */
    @Test
    public void testDataForEuropeanCountries() throws InvalidNumberOfArgumentsException{
        TrackClientBrowserDetails.main(new String[] {"src/test/resources/ccds2.tsv", "src/test/resources/ccds1.csv"});
        Set<CountryData> countries = TrackClientBrowserDetails.readFromTSV("src/test/resources/ccds2.tsv").stream().collect(Collectors.toSet());
        Set<CountryData> inputCountries = TrackClientBrowserDetails.readFromTSV("src/test/resources/ccds2.tsv").stream().filter(TrackClientBrowserDetails.NON_EU_COUNTRIES).collect(Collectors.toSet());
        String homeDir = System.getProperty("user.home");
        Set<CountryData> result = readFromTSV(homeDir+"/output.txt").stream().collect(Collectors.toSet());
        // Validates no European country present in result
        assert(result.stream().filter(EU_COUNTRIES).count() == 0);
        // Validates the input entries did not got missed out in output.
        assert(inputCountries.size()==result.size());
        //Negative test case to prove correctness of the solution provided
        long countOfEU = countries.stream().filter(EU_COUNTRIES).count();
        assert(countOfEU == (countries.size() - result.size()));
    }
    
    
    private static Set<CountryData> readFromTSV(String fileName) {
        Set<CountryData> countries = new HashSet<>();
        Path pathToFile = Paths.get(fileName);
        try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)) {
            countries = br.lines().map(mapToCountry).collect(Collectors.toSet());
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return countries;
    }
    private static Function<String, CountryData> mapToCountry = (line) -> {
        String[] attributes = line.split("\\s+");
        CountryData country = new CountryData();
        country.setCountryId(attributes[0]);
        country.setCountry(attributes[1]);
        if (attributes.length > 2) {
            country.setUserAgent(StringUtil.value(attributes[2], StringUtil.EMPTY_STRING));
        }
        if (attributes.length > 3) {
            country.setField2(StringUtil.value(attributes[3], StringUtil.EMPTY_STRING));
        }
        if (attributes.length > 4) {
            country.setField2(StringUtil.value(attributes[4], StringUtil.EMPTY_STRING));
        }
        if (attributes.length > 5) {
            country.setField3(StringUtil.value(attributes[5], StringUtil.EMPTY_STRING));
        }
        return country;
    };
}
