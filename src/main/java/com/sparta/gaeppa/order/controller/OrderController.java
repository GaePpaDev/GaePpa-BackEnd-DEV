package com.sparta.gaeppa.order.controller;

import static com.sparta.gaeppa.global.util.ApiResponseUtil.success;

import com.sparta.gaeppa.global.util.ApiResponseUtil.ApiResult;
import com.sparta.gaeppa.order.dto.OrderAndPaymentRequestDto;
import com.sparta.gaeppa.order.dto.OrderListResponseDto;
import com.sparta.gaeppa.order.dto.OrderRequestDto;
import com.sparta.gaeppa.order.dto.OrderResponseDto;
import com.sparta.gaeppa.order.service.OrderService;
import com.sparta.gaeppa.payment.dto.PaymentDto;
import com.sparta.gaeppa.payment.service.PaymentsService;
import com.sparta.gaeppa.security.jwts.entity.CustomUserDetails;
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
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final PaymentsService paymentsService;

    @GetMapping
    public ResponseEntity<ApiResult<OrderListResponseDto>> getAllOrdersByMemberId(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        OrderListResponseDto responseDto = orderService.getAllOrdersByMemberId(userDetails.getMemberId());

        return new ResponseEntity<>(success(responseDto), HttpStatus.OK);
    }

    @GetMapping("/orderId")
    public ResponseEntity<ApiResult<OrderResponseDto>> getAllOrdersByOrderId(
            @RequestParam("orderId") UUID orderId) {

        OrderResponseDto responseDto = orderService.getOrderByOrderId(orderId);

        return new ResponseEntity<>(success(responseDto), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResult<OrderResponseDto>> createOrder(@RequestBody OrderAndPaymentRequestDto requestDto,
                                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {

        requestDto.setMemberIdByUserDetail(userDetails.getMemberId());
        OrderResponseDto responseDto = orderService.createOrder(OrderRequestDto.from(requestDto));

        PaymentDto paymentDto = PaymentDto.from(requestDto);
        paymentDto.setOrderId(responseDto.getOrderId());
        paymentsService.createPayment(paymentDto);

        return new ResponseEntity<>(success(responseDto), HttpStatus.CREATED);
    }


    @PatchMapping
    public ResponseEntity<ApiResult<String>> cancelOrder(@RequestParam("orderId") UUID orderId) {

        orderService.cancelOrder(orderId);
        paymentsService.cancelPaymentByOrderId(orderId);

        return new ResponseEntity<>(success("Cancel Order Success"), HttpStatus.OK);
    }
}
