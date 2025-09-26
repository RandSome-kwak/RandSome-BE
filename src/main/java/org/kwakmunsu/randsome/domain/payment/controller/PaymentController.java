package org.kwakmunsu.randsome.domain.payment.controller;

import lombok.RequiredArgsConstructor;
import org.kwakmunsu.randsome.domain.payment.serivce.PaymentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@RestController
public class PaymentController {

    private final PaymentService paymentService;

}