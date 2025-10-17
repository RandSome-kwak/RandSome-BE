package org.kwakmunsu.randsome.admin.matching.repository.matching;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.admin.matching.service.MatchingAdminRepository;
import org.kwakmunsu.randsome.domain.matching.entity.Matching;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MatchingAdminRepositoryImpl implements MatchingAdminRepository {

    private final MatchingAdminJpaRepository matchingAdminJpaRepository;

    // 최대 5건이라 JPA saveAll 사용
    @Override
    public void saveAll(List<Matching> results) {
        matchingAdminJpaRepository.saveAll(results);
    }

}