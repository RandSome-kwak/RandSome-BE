package org.kwakmunsu.randsome;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.kwakmunsu.randsome.domain.admin.controller.AdminController;
import org.kwakmunsu.randsome.domain.admin.serivce.AdminService;
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
import org.kwakmunsu.randsome.global.TestSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@Import(TestSecurityConfig.class)
@WebMvcTest(
        controllers = {
                AdminController.class,
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
    protected InquiryService inquiryService;

    @MockitoBean
    protected MatchingService matchingService;

    @MockitoBean
    protected AdminService adminService;

    @MockitoBean
    protected AuthService authService;

}