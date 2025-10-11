package org.kwakmunsu.randsome.domain.matching.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kwakmunsu.randsome.domain.BaseEntity;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingStatus;
import org.kwakmunsu.randsome.domain.matching.enums.MatchingType;
import org.kwakmunsu.randsome.domain.member.entity.Member;

@Table(name = "matching_applications")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MatchingApplication extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private Member requester;

    private int requestedCount; // 요청한 인원 수

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchingStatus matchingStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatchingType matchingType;

    // 조회용 양방향
    @OneToMany(mappedBy = "application", fetch = FetchType.LAZY)
    private List<Matching> matchings = new ArrayList<>();

    public static MatchingApplication create(Member requester, MatchingType matchingType, int requestedCount) {
        MatchingApplication application = new MatchingApplication();

        application.requester = requester;
        application.matchingType = matchingType;
        application.requestedCount = requestedCount;
        application.matchingStatus = MatchingStatus.PENDING;

        return application;
    }

    public void complete() {
        this.matchingStatus = MatchingStatus.COMPLETED;
    }

    public void fail() {
        this.matchingStatus = MatchingStatus.FAILED;
    }

    public boolean isComplete() {
        return this.matchingStatus == MatchingStatus.COMPLETED;
    }

    public boolean isOwnedBy(Long requesterId) {
        return requester.getId().equals(requesterId);
    }

}