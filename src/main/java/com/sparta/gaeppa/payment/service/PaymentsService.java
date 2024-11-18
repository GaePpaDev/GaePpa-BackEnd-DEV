package com.sparta.gaeppa.payment.service;

import com.sparta.gaeppa.global.exception.ExceptionStatus;
import com.sparta.gaeppa.global.exception.ServiceException;
import com.sparta.gaeppa.order.entity.OrderStatus;
import com.sparta.gaeppa.order.entity.Orders;
import com.sparta.gaeppa.order.repository.OrderRepository;
import com.sparta.gaeppa.payment.dto.PaymentDto;
import com.sparta.gaeppa.payment.entity.Payments;
import com.sparta.gaeppa.payment.repository.PaymentsRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentsService {

    private final PaymentsRepository paymentsRepository;
    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    public PaymentDto getPaymentByOrderId(UUID orderId) {

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ServiceException(ExceptionStatus.ORDER_NOT_FOUND));

        return Optional.ofNullable(paymentsRepository.findPaymentByOrder(order))
                .map(PaymentDto::from)
                .orElseThrow(() -> new ServiceException(ExceptionStatus.PAYMENT_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<PaymentDto> getAllPaymentsByMemberId(UUID memberId) {

        return Optional.of(paymentsRepository.findAllPaymentsByMemberId(memberId))
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new ServiceException(ExceptionStatus.PAYMENT_NOT_FOUND))
                .stream()
                .map(PaymentDto::from)
                .toList();
    }

    @Transactional
    public PaymentDto createPayment(PaymentDto requestDto) {

        Orders orders = orderRepository.findById(requestDto.getOrderId())
                .orElseThrow(() -> new ServiceException(ExceptionStatus.ORDER_NOT_FOUND));

        if (orders.getOrderStatus().equals(OrderStatus.CANCELED)) {
            throw new ServiceException(ExceptionStatus.PAYMENT_MODIFICATION_NOT_ALLOWED);
        }

        return PaymentDto.from(paymentsRepository.save(requestDto.toEntity(orders)));
    }

    @Transactional
    public void cancelPaymentByPayId(UUID payId) {

        Payments payments = paymentsRepository.findById(payId)
                .orElseThrow(() -> new ServiceException(ExceptionStatus.PAYMENT_NOT_FOUND));

        payments.cancel(payId);

    }

    @Transactional
    public void cancelPaymentByOrderId(UUID orderId) {

        Orders orders = orderRepository.findById(orderId)
                .orElseThrow(() -> new ServiceException(ExceptionStatus.ORDER_NOT_FOUND));

        Payments payments = paymentsRepository.findPaymentByOrder(orders);
        payments.cancel(payments.getPayId());

    }


}
