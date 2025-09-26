package org.kwakmunsu.randsome.domain.inquiry.controller;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.inquiry.serivce.InquiryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/inquiries")
@RequiredArgsConstructor
@RestController
public class InquiryController {

    private final InquiryService inquiryService;

}