package org.kwakmunsu.randsome.domain.member;

import org.kwakmunsu.randsome.domain.member.controller.dto.MemberRegisterRequest;
import org.kwakmunsu.randsome.domain.member.entity.Member;
import org.kwakmunsu.randsome.domain.member.enums.Gender;
import org.kwakmunsu.randsome.domain.member.enums.Mbti;
import org.springframework.test.util.ReflectionTestUtils;

/*
 * 테스트 코드에서 반복적으로 사용되는 도메인의 관련 데이터를 모아둔 클래스
 **/
public class MemberFixture {

    public static MemberRegisterRequest createMemberRegisterRequest(String loginId, String nickname) {
        return new MemberRegisterRequest(
                loginId,
                "password",
                nickname,
                Gender.M,
                Mbti.ENFJ,
                "instagramId",
                "introduction",
                "idealDescription"
        );
    }

    public static MemberRegisterRequest createMemberRegisterRequest() {
        return createMemberRegisterRequest("iii6602", "nickname");
    }

    public static Member createMember() {
        var serviceRequest = createMemberRegisterRequest().toServiceRequest();
        return serviceRequest.toEntity("encryptedPassword");
    }

    public static Member createMember(String loginId, String nickname) {
        var serviceRequest = createMemberRegisterRequest(loginId, nickname).toServiceRequest();
        return serviceRequest.toEntity("encryptedPassword");
    }

    public static Member createMember(Long id) {
        var serviceRequest = createMemberRegisterRequest().toServiceRequest();
        Member member = serviceRequest.toEntity("encryptedPassword");
        ReflectionTestUtils.setField(member, "id", id);
        return member;
    }

}