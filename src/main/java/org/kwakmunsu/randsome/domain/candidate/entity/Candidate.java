package org.kwakmunsu.randsome.domain.candidate.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kwakmunsu.randsome.domain.BaseEntity;
import org.kwakmunsu.randsome.domain.candidate.enums.CandidateStatus;
import org.kwakmunsu.randsome.domain.member.entity.Member;

@Table(name = "candidates")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Candidate extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(name = "candidate_status", nullable = false)
    private CandidateStatus status;

    public static Candidate create(Member member) {
        Candidate candidate = new Candidate();

        candidate.member = member;
        candidate.status = CandidateStatus.PENDING;

        return candidate;
    }

    public void approve() {
        this.status = CandidateStatus.APPROVED;
    }

    public void reject() {
        this.status = CandidateStatus.REJECTED;
    }

}