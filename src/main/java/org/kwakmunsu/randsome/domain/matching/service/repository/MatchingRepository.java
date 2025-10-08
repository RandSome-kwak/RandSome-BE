package org.kwakmunsu.randsome.domain.matching.service.repository;

import java.util.List;
import org.kwakmunsu.randsome.domain.matching.entity.Matching;

public interface MatchingRepository {

    void saveAll(List<Matching> results);

}