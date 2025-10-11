package org.kwakmunsu.randsome.domain.inquiry.controller;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.inquiry.controller.dto.InquiryRegisterRequest;
import org.kwakmunsu.randsome.domain.inquiry.serivce.InquiryService;
import org.kwakmunsu.randsome.global.annotation.AuthMember;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/inquiries")
@RequiredArgsConstructor
@RestController
public class InquiryController extends InquiryDocsController {

    private final InquiryService inquiryService;

    @Override
    @PostMapping
    public ResponseEntity<Long> register(@AuthMember Long memberId, InquiryRegisterRequest request) {
        Long inquiryId = inquiryService.registerInquiry(request.toServiceRequest(memberId));

        return ResponseEntity.status(HttpStatus.CREATED).body(inquiryId);
    }

}