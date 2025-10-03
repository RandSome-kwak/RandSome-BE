package org.kwakmunsu.randsome.domain.matching.serivce.repository;

import org.kwakmunsu.randsome.domain.matching.entity.MatchingApplication;

public interface MatchingApplicationRepository {

    MatchingApplication save(MatchingApplication matchingApplication);
    MatchingApplication findById(Long id);
}