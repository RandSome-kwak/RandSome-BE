package org.kwakmunsu.randsome.admin.matching.repository.matching;

import org.kwakmunsu.randsome.domain.matching.entity.Matching;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingAdminJpaRepository extends JpaRepository<Matching, Long> {

}