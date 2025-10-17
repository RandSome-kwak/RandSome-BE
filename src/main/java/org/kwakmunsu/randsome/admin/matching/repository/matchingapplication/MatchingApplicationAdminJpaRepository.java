package org.kwakmunsu.randsome.admin.matching.repository.matchingapplication;

import org.kwakmunsu.randsome.domain.matching.entity.MatchingApplication;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingApplicationAdminJpaRepository extends JpaRepository<MatchingApplication, Long> {

    long countByStatus(MatchingStatus matchingStatus);

}