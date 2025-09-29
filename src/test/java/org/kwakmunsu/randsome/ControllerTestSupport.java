package org.kwakmunsu.randsome;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.kwakmunsu.randsome.admin.candidate.controller.CandidateAdminController;
import org.kwakmunsu.randsome.admin.candidate.serivce.CandidateAdminService;
import org.kwakmunsu.randsome.domain.auth.controller.AuthController;
import org.kwakmunsu.randsome.domain.auth.serivce.AuthService;
import org.kwakmunsu.randsome.domain.candidate.controller.CandidateController;
import org.kwakmunsu.randsome.domain.candidate.serivce.CandidateService;
import org.kwakmunsu.randsome.domain.inquiry.controller.InquiryController;
import org.kwakmunsu.randsome.domain.inquiry.serivce.InquiryService;
import org.kwakmunsu.randsome.domain.matching.controller.MatchingController;
import org.kwakmunsu.randsome.domain.matching.serivce.MatchingService;
import org.kwakmunsu.randsome.domain.member.controller.MemberController;
import org.kwakmunsu.randsome.domain.member.serivce.MemberService;
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
                MemberController.class,
                AuthController.class,
                CandidateController.class,
                InquiryController.class,
                MatchingController.class
        })
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvcTester mvcTester;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockitoBean
    protected MemberService memberService;

    @MockitoBean
    protected CandidateService candidateService;

    @MockitoBean
    protected CandidateAdminService candidateAdminService;

    @MockitoBean
    protected InquiryService inquiryService;

    @MockitoBean
    protected MatchingService matchingService;

    @MockitoBean
    protected AuthService authService;

}