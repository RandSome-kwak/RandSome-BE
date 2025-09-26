package org.kwakmunsu.randsome.domain.inquiry.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kwakmunsu.randsome.domain.BaseEntity;

@Table(name = "inquiries")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Inquiry extends BaseEntity {

}