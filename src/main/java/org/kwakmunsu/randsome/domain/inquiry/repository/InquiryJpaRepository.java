package org.kwakmunsu.randsome.domain.inquiry.repository;

import java.util.List;
import java.util.Optional;
import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InquiryJpaRepository extends JpaRepository<Inquiry, Long> {

    @Query("SELECT i FROM Inquiry i JOIN FETCH i.author WHERE i.author.id = :authorId")
    List<Inquiry> findAllByAuthorId(@Param("authorId") Long authorId);

    Optional<Inquiry> findByIdAndAuthorId(Long id, Long authorId);

}