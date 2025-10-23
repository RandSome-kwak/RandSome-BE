package org.kwakmunsu.randsome.domain.inquiry.repository;

import java.util.List;
import java.util.Optional;
import org.kwakmunsu.randsome.domain.EntityStatus;
import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InquiryJpaRepository extends JpaRepository<Inquiry, Long> {

    @Query("SELECT i FROM Inquiry i JOIN FETCH i.author WHERE i.author.id = :authorId AND i.status = :status")
    List<Inquiry> findAllByAuthorIdAndStatus(@Param("authorId") Long authorId, @Param("status") EntityStatus status);

    Optional<Inquiry> findByIdAndAuthorIdAndStatus(Long id, Long authorId, EntityStatus status);

    long countByAuthorIdAndStatus(Long authorId, EntityStatus status);

}