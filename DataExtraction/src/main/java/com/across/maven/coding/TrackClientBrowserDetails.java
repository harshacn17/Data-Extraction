package com.across.maven.coding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.across.maven.coding.dto.CountryData;
import com.across.maven.coding.dto.UserAgentData;
import com.across.maven.coding.exception.InvalidNumberOfArgumentsException;
import com.across.maven.coding.utililty.StringUtil;

/**
 * With the provided data sets (ccds1.csv and ccds2.tsv) 
 * Set up a Java project with Gradle or Maven. 
 *  It should not use MapReduce, Spark, etc. 
 *  Write out the data... 
 * o with fields in this order: ID, COUNTRY, UA, F1, F2, F3, 
 * o and with all European Union countries filtered out.
 */
public class TrackClientBrowserDetails {
    static Predicate<CountryData> NON_EU_COUNTRIES = country -> !Arrays.asList("AT", "BE", "BG", "CY", "CZ", "DE", "DK", "EE", "ES", "FI", "FR", "GB",
                "GR", "HR", "HU", "IE", "IT", "LT", "LU", "LV", "MT", "NL", "PO", "PT", "RO", "SE", "SI", "SK").contains(country.getCountry());

    public static void main(String... args) throws InvalidNumberOfArgumentsException {

        if (args.length != 2) {
            throw new InvalidNumberOfArgumentsException("Exception Occured");
        }
        if (StringUtil.isNullOrEmpty(args[0]) || StringUtil.isNullOrEmpty(args[1])) {
            System.out.println(
                        "Did you provide input files ?\nPlease provide path of Country(ccds2.tsv) & User-Agent(ccds1.csv) files as 1st and 2nd argument respectively");
            return;
        }
        long startTime = System.currentTimeMillis();
        Set<CountryData> countries = readFromTSV(args[0]).stream().filter(NON_EU_COUNTRIES).collect(Collectors.toSet());
        Map<String, UserAgentData> userAgents = readFromCSV(args[1]).stream().collect(Collectors.toMap(UserAgentData::getId, Function.identity()));
        long endTime = System.currentTimeMillis();
        System.out.println("Took " + (endTime - startTime) + " ms" + " to read the files");
        getOutPutFile(countries, userAgents);
    }

    /**
     * Takes arguments as input and writes to output.txt at home folder
     * 
     * @param args
     */
    public static Boolean getOutPutFile(Set<CountryData> countries, Map<String, UserAgentData> userAgents) {
        // get the home folder of the system
        String homeDir = System.getProperty("user.home");
        Boolean success = Boolean.FALSE;
        System.out.println("The out put file will be available at path : " + homeDir);

        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Paths.get(homeDir + "/output.txt")))) {
            countries.stream().forEach(country -> {
                pw.print(mapToResult.apply(userAgents, country).toString());
            });
            success = Boolean.TRUE;
        }
        catch (IOException e) {
            e.printStackTrace();
            success = Boolean.FALSE;
        }
        return success;
    }

    /**
     * Java Functional BiFunnction to apply the condition on each of the stream object
     */
    private static BiFunction<Map<String, UserAgentData>, CountryData, CountryData> mapToResult = (userAgents, country) -> {
        UserAgentData userAgentData = userAgents.get(country.getCountryId());
        String userAgent = StringUtil.EMPTY_STRING;
        if (null != userAgentData)
            userAgent = userAgentData.getUserAgent();
        return new CountryData(country.getCountryId(), country.getCountry(), userAgent, country.getField1(), country.getField2(), country.getField3());
    };

    /**
     * Reads the entire lines at ones and uses Java 8 feature to map at ones. There by avoiding each line till the end
     * of the file.
     * 
     * @param fileName
     * @return
     */
    static Set<CountryData> readFromTSV(String fileName) {
        Set<CountryData> countries = new HashSet<>();
        Path pathToFile = Paths.get(fileName);
        // create an instance of BufferedReader
        // using try with resource, Java 7 feature to close resources
        try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)) {
            countries = br.lines().map(mapToCountry).collect(Collectors.toSet());
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return countries;
    }

    static Set<UserAgentData> readFromCSV(String fileName) {
        Set<UserAgentData> userAgents = new HashSet<>();
        Path pathToFile = Paths.get(fileName);
        try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)) {
            userAgents = br.lines().map(mapToUserAgents).collect(Collectors.toSet());
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return userAgents;
    }

    /**
     * Used Java 8 Function interface Helps to tokenize the input and maps to object considering null checks of keys
     * which are candidates for empty or null values.
     */
    private static Function<String, CountryData> mapToCountry = (line) -> {
        String[] attributes = line.split("\\s+");
        CountryData country = new CountryData();
        country.setCountryId(attributes[0]);
        country.setCountry(attributes[1]);
        if (attributes.length > 2) {
            country.setField1(StringUtil.value(attributes[2], StringUtil.EMPTY_STRING));
        }
        if (attributes.length > 3) {
            country.setField2(StringUtil.value(attributes[3], StringUtil.EMPTY_STRING));
        }
        if (attributes.length > 4) {
            country.setField3(StringUtil.value(attributes[4], StringUtil.EMPTY_STRING));
        }
        return country;
    };

    private static Function<String, UserAgentData> mapToUserAgents = (line) -> {
        String[] attributes = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        UserAgentData userAgentData = new UserAgentData();
        userAgentData.setId(attributes[0]);
        if (attributes.length > 1) {
            userAgentData.setUserAgent(StringUtil.value(attributes[1], StringUtil.EMPTY_STRING));
        }
        return userAgentData;
    };

}

