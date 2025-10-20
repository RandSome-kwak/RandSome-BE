package org.kwakmunsu.randsome.admin.inquiry.repository;

import org.kwakmunsu.randsome.domain.EntityStatus;
import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;
import org.kwakmunsu.randsome.domain.inquiry.enums.InquiryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryAdminJpaRepository extends JpaRepository<Inquiry, Long> {

    long countByInquiryStatusAndStatus(InquiryStatus inquiryStatus, EntityStatus status);

}