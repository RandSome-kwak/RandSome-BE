package org.kwakmunsu.randsome.admin.matching.repository.matching;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.matching.entity.Matching;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MatchingAdminRepository {
    private final MatchingAdminJpaRepository matchingAdminJpaRepository;

    // 최대 5건이라 JPA saveAll 사용
    public void saveAll(List<Matching> results) {
        matchingAdminJpaRepository.saveAll(results);
    }

}