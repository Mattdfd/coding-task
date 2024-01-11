package com.minnity.report;

import org.apache.commons.lang3.NotImplementedException;

import java.util.*;
import java.util.stream.Collectors;

public class ReportService {

  //task 1: Return number of requests that were made for each company. (e.g. companyId -> requestNumber)
  public Map<Integer, Long> calculateNumberOfRequestsPerCompany(List<RequestLog> requestLogs) {
    //initialize Map
    Map<Integer, Long> numberOfRequestsPerCompany =new HashMap<>();

    //Loop through the requestLogs, check if map contains companyid, if it does add 1 to the current count, if not add 1
    for (RequestLog log : requestLogs) {
      int companyId = log.getCompanyId();
      if (numberOfRequestsPerCompany.containsKey(companyId)) {
        long currentCount = numberOfRequestsPerCompany.get(companyId);
        numberOfRequestsPerCompany.put(companyId, currentCount + 1);
      } else {
        numberOfRequestsPerCompany.put(companyId, 1L);
      }

    }
    return numberOfRequestsPerCompany;
  }

  //task 2: Count and return requests per company that finished with an error HTTP response code (>=400)
  public Map<Integer, List<RequestLog>> findRequestsWithError(List<RequestLog> requestLogs) {
    Map<Integer, List<RequestLog>> requestsWithError =new HashMap<>();

    //loop through the request logs, and check for request status' of >=400
    for (RequestLog log : requestLogs){
      if(log.getRequestStatus()>=400){
        int companyId = log.getCompanyId();
        List<RequestLog> logsWithError = requestsWithError.get(companyId);

        //if the list doesnt exist for the company create a new list and add the list to the map requestsWithError
        if(logsWithError ==null){
          logsWithError = new ArrayList<>();
          requestsWithError.put(companyId,logsWithError);
        }
        //add the log with error to the list
        logsWithError.add(log);
      }
    }
    return requestsWithError;
  }

  //task 3: find and print API (requests path) that on average takes the longest time to process the request.
  public String findRequestPathWithLongestDurationTime(List<RequestLog> requestLogs) {

    //map holds the groups of requestpaths and their calculated avg of duration of requests
    Map<String,Double> pathAndDurationMap =
            requestLogs.stream().collect(Collectors.groupingBy(RequestLog::getRequestPath, Collectors.averagingDouble(RequestLog::getRequestDuration)));

    //another map to hold the highest avg value in the pathAndDurationMap
    Optional<Map.Entry<String, Double>> maxEntry = pathAndDurationMap.entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue));

    //returning the key or path for the highest avg request duration
    return maxEntry.map(Map.Entry::getKey).orElse(null);
  }
}
