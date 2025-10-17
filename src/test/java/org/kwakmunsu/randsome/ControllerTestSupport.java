package org.kwakmunsu.randsome;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.kwakmunsu.randsome.admin.candidate.controller.CandidateAdminController;
import org.kwakmunsu.randsome.admin.candidate.serivce.CandidateAdminService;
import org.kwakmunsu.randsome.admin.inquiry.controller.InquiryAdminController;
import org.kwakmunsu.randsome.admin.inquiry.serivce.InquiryAdminService;
import org.kwakmunsu.randsome.admin.matching.controller.MatchingAdminController;
import org.kwakmunsu.randsome.admin.matching.service.MatchingAdminService;
import org.kwakmunsu.randsome.admin.member.controller.MemberAdminController;
import org.kwakmunsu.randsome.admin.member.service.MemberAdminService;
import org.kwakmunsu.randsome.admin.statistics.controller.StatisticsAdminController;
import org.kwakmunsu.randsome.admin.statistics.service.StatisticsAdminService;
import org.kwakmunsu.randsome.domain.auth.controller.AuthController;
import org.kwakmunsu.randsome.domain.auth.service.AuthService;
import org.kwakmunsu.randsome.domain.candidate.controller.CandidateController;
import org.kwakmunsu.randsome.domain.candidate.service.CandidateService;
import org.kwakmunsu.randsome.domain.inquiry.controller.InquiryController;
import org.kwakmunsu.randsome.domain.inquiry.service.InquiryCommandService;
import org.kwakmunsu.randsome.domain.inquiry.service.InquiryQueryService;
import org.kwakmunsu.randsome.domain.matching.controller.MatchingController;
import org.kwakmunsu.randsome.domain.matching.service.MatchingCommandService;
import org.kwakmunsu.randsome.domain.matching.service.MatchingQueryService;
import org.kwakmunsu.randsome.domain.member.controller.MemberController;
import org.kwakmunsu.randsome.domain.member.service.MemberCommandService;
import org.kwakmunsu.randsome.domain.member.service.MemberQueryService;
import org.kwakmunsu.randsome.global.security.TestSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@Import(TestSecurityConfig.class)
@WebMvcTest(
        controllers = {
                CandidateAdminController.class,
                CandidateController.class,
                MemberAdminController.class,
                MemberController.class,
                AuthController.class,
                InquiryAdminController.class,
                InquiryController.class,
                MatchingAdminController.class,
                MatchingController.class,
                StatisticsAdminController.class
        })
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvcTester mvcTester;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockitoBean
    protected MemberQueryService memberQueryService;

    @MockitoBean
    protected MemberCommandService memberCommandService;

    @MockitoBean
    protected MemberAdminService memberAdminService;

    @MockitoBean
    protected CandidateService candidateService;

    @MockitoBean
    protected CandidateAdminService candidateAdminService;

    @MockitoBean
    protected InquiryQueryService inquiryQueryService;

    @MockitoBean
    protected InquiryCommandService inquiryCommandService;

    @MockitoBean
    protected InquiryAdminService inquiryAdminService;

    @MockitoBean
    protected MatchingQueryService matchingQueryService;

    @MockitoBean
    protected MatchingCommandService matchingCommandService;

    @MockitoBean
    protected MatchingAdminService matchingAdminService;

    @MockitoBean
    protected AuthService authService;

    @MockitoBean
    protected StatisticsAdminService statisticsAdminService;

}