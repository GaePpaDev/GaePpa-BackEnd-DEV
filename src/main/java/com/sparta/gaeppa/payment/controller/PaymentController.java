package com.sparta.gaeppa.payment.controller;

import static com.sparta.gaeppa.global.util.ApiResponseUtil.success;

import com.sparta.gaeppa.global.util.ApiResponseUtil.ApiResult;
import com.sparta.gaeppa.payment.dto.PaymentDto;
import com.sparta.gaeppa.payment.service.PaymentsService;
import com.sparta.gaeppa.security.jwts.entity.CustomUserDetails;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentsService paymentsService;

    @GetMapping
    public ResponseEntity<ApiResult<PaymentDto>> getPaymentByOrderId(
            @RequestParam("orderId") UUID orderId) {

        PaymentDto responseDto = paymentsService.getPaymentByOrderId(orderId);

        return new ResponseEntity<>(success(responseDto), HttpStatus.OK);
    }

    @GetMapping("/memberId")
    public ResponseEntity<ApiResult<List<PaymentDto>>> getAllPaymentsByMemberId(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        List<PaymentDto> responseDto = paymentsService.getAllPaymentsByMemberId(userDetails.getMemberId());

        return new ResponseEntity<>(success(responseDto), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResult<PaymentDto>> createPayment(@RequestBody PaymentDto requestDto) {

        PaymentDto responseDto = paymentsService.createPayment(requestDto);

        return new ResponseEntity<>(success(responseDto), HttpStatus.CREATED);
    }

    @PatchMapping
    public ResponseEntity<ApiResult<String>> cancelPayment(@RequestParam("payId") UUID payId) {

        paymentsService.cancelPaymentByPayId(payId);

        return new ResponseEntity<>(success("Cancel Payment Success"), HttpStatus.OK);
    }
}
