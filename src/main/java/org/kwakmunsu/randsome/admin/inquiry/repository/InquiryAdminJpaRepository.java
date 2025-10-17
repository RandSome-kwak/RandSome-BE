package org.kwakmunsu.randsome.admin.inquiry.repository;

import org.kwakmunsu.randsome.domain.inquiry.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryAdminJpaRepository extends JpaRepository<Inquiry, Long> {

}