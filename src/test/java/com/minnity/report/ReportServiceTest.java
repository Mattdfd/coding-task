package com.minnity.report;

import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;


public class ReportServiceTest {

  @Test
  public void testCalculateNumberOfRequestsPerCompanyWithRandomData() {

    //using SampleDataGenerator to create data
    List<RequestLog> logs = new ArrayList<>();
    for (int i = 0; i < 50; i++) {
      logs.add(SampleDataGenerator.aRequestLog());
    }

    ReportService reportService = new ReportService();

    //call task1 method for testing
    Map<Integer, Long> requestCount = reportService.calculateNumberOfRequestsPerCompany(logs);

    int expectedCountForCompany = 0;

    for (RequestLog log : logs) {
      if (log.getCompanyId() == 4) {
        expectedCountForCompany++;
      }
    }
    System.out.println("Expected count: "+expectedCountForCompany);

    //testing to see if the expected number of requests for company with ID 4 matches the actual count.
    assertEquals(expectedCountForCompany, requestCount.getOrDefault(4, 0L).intValue());

    System.out.println("Actual count: "+ requestCount.getOrDefault(4,0L).intValue());
  }


  @Test
  public void testFindRequestsWithError(){

    //using SampleDataGenerator to create data
    List<RequestLog> logs = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      logs.add(SampleDataGenerator.aRequestLog());
    }

    ReportService reportService = new ReportService();

    //call task2 method for testing
    Map<Integer, List<RequestLog>> requestLogsMap = reportService.findRequestsWithError(logs);
    System.out.println(requestLogsMap.toString());

    //iterate over the logs and calculate the expected count for each company with an error status
    Map<Integer, Long> expectedCountPerCompany = logs.stream().filter(log -> log.getRequestStatus() >= 400)
            .collect(Collectors.groupingBy(RequestLog::getCompanyId, Collectors.counting()));

    System.out.println(expectedCountPerCompany.toString());

    int companyIdToTest = 4;

    //testing for individual company using companyId.  gets value for the key.
    long expectedCount = expectedCountPerCompany.getOrDefault(companyIdToTest, 0L);

    List<RequestLog> requestLogsForCompany = requestLogsMap.getOrDefault(companyIdToTest, new ArrayList<>());

    //filters request logs for company by >=400 to get actual count
    long actualCount = requestLogsForCompany.stream().filter(log -> log.getRequestStatus() >= 400).count();

    assertEquals(expectedCount, actualCount);
    System.out.println("Expected: " + expectedCount + "\n" + "Actual: " + actualCount);

  }

  @Test
  public void TestFindRequestPathWithLongestDurationTime() {

    //using SampleDataGenerator to create data
    List<RequestLog> logs = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      logs.add(SampleDataGenerator.aRequestLog());
    }

    //map holds the groups of requestpaths and their calculated avg of duration of requests
    Map<String, Double> pathAndDurationMap =
            logs.stream().collect(Collectors.groupingBy(RequestLog::getRequestPath,Collectors.averagingDouble(RequestLog::getRequestDuration)));

    System.out.println(pathAndDurationMap.toString());
    ReportService reportService = new ReportService();

    //another map to hold the highest avg value in the pathAndDurationMap
    Optional<Map.Entry<String, Double>> maxEntry = pathAndDurationMap.entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue));

    //expected value comes from the calculated avg duration from sample data set from above
    String expected =maxEntry.map(Map.Entry::getKey).orElse(null);

    //actual value comes from the method in ReportService
    String actual = reportService.findRequestPathWithLongestDurationTime(logs);

    assertEquals(expected,actual);
    System.out.println("Expected: " +expected+ "\n"+ "Actual: "+ actual);
  }
  }