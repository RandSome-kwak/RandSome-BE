package org.kwakmunsu.randsome.domain.matching.repository;

import org.kwakmunsu.randsome.domain.matching.entity.Matching;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingJpaRepository extends JpaRepository<Matching, Long> {

}