package org.kwakmunsu.randsome.domain.inquiry.repository;

import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryJpaRepository extends JpaRepository<Inquiry, Long> {

}