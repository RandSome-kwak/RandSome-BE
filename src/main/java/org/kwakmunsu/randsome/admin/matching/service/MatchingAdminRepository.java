package org.kwakmunsu.randsome.admin.matching.service;

import java.util.List;
import org.kwakmunsu.randsome.domain.matching.entity.Matching;

public interface MatchingAdminRepository {

    void saveAll(List<Matching> results);

}