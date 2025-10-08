package org.kwakmunsu.randsome.domain.matching.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.matching.entity.Matching;
import org.kwakmunsu.randsome.domain.matching.service.repository.MatchingRepository;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MatchingRepositoryImpl implements MatchingRepository {

    private final MatchingJpaRepository matchingJpaRepository;

    // 최대 5건이라 JPA saveAll 사용
    @Override
    public void saveAll(List<Matching> results) {
        matchingJpaRepository.saveAll(results);
    }

}