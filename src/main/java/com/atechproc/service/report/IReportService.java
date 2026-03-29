package com.atechproc.service.report;

import com.atechproc.model.Report;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface IReportService {
    Report getTodayReport(String jwt) throws Exception;
    Report getThisMonthReport(String jwt) throws Exception;
    Report getThisYearReport(String jwt) throws Exception;
    Report getThisWeekReport(String jwt) throws Exception;

    Report getDayReport(String jwt, LocalDate date) throws Exception;
    Report getMonthReport(String jwt, YearMonth yearMonth) throws Exception;
    Report getYearReport(String jwt, int year) throws Exception;
    Report getWeekOfMonthReport(String jwt, int weekOfMonth, YearMonth yearMonth) throws Exception;
}
