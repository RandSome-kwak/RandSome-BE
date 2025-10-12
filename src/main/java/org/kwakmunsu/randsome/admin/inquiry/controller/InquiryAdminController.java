package org.kwakmunsu.randsome.admin.inquiry.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.admin.inquiry.controller.dto.AnswerRegisterRequest;
import org.kwakmunsu.randsome.admin.inquiry.serivce.InquiryAdminService;
import org.kwakmunsu.randsome.global.annotation.AuthMember;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/admin/inquiries")
@RequiredArgsConstructor
@RestController
public class InquiryAdminController extends InquiryAdminDocsController {

    private final InquiryAdminService inquiryAdminService;

    @Override
    @PatchMapping("/{inquiryId}/answers")
    public ResponseEntity<Void> registerAnswer(@PathVariable Long inquiryId, @Valid @RequestBody AnswerRegisterRequest request) {
        inquiryAdminService.registerAnswer(inquiryId, request.answer());

        return ResponseEntity.ok().build();
    }

}