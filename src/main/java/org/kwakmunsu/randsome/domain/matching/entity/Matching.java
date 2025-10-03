package org.kwakmunsu.randsome.domain.matching.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.kwakmunsu.randsome.domain.BaseEntity;
import org.kwakmunsu.randsome.domain.member.entity.Member;

@Table(name = "matchings")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Matching extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "application_id")
    private MatchingApplication application;

    @ManyToOne
    @JoinColumn(name = "selected_member_id")
    private Member selectedMember;

    public static Matching create(MatchingApplication application, Member selectedMember) {
        Matching matching = new Matching();

        matching.application = application;
        matching.selectedMember = selectedMember;

        return matching;
    }
}