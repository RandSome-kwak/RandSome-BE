package org.kwakmunsu.randsome.admin.statistics.service;

public interface StatisticsRepository {

    long findPendingApprovals(String pendingStatus);

}