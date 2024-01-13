package com.minnity.report;

import org.apache.commons.lang3.NotImplementedException;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class ReportService {

  //task 1: Return number of requests that were made for each company. (e.g. companyId -> requestNumber)
  public Map<Integer, Long> calculateNumberOfRequestsPerCompany(List<RequestLog> requestLogs) {

    return requestLogs.stream()
            .collect(Collectors.groupingBy(RequestLog::getCompanyId,Collectors.counting()));
  }

  //task 2: Count and return requests per company that finished with an error HTTP response code (>=400)
  public Map<Integer, List<RequestLog>> findRequestsWithError(List<RequestLog> requestLogs) {

    return requestLogs.stream()
            .filter(x ->x.getRequestStatus() >= 400)
            .collect(Collectors.groupingBy(RequestLog::getCompanyId));
  }

  //task 3: find and print API (requests path) that on average takes the longest time to process the request.
  public String findRequestPathWithLongestDurationTime(List<RequestLog> requestLogs) {

    return requestLogs.stream()
            .collect(Collectors.groupingBy(RequestLog::getRequestPath,Collectors.averagingDouble(RequestLog::getRequestDuration)))
            .entrySet().stream()
            .max(Comparator.comparingDouble(Map.Entry::getValue))
            .map(Map.Entry::getKey).orElse(null);
  }
}
