package org.kwakmunsu.randsome.domain.matching.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kwakmunsu.randsome.domain.BaseEntity;

@Table(name = "matchings")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Matching extends BaseEntity {

}