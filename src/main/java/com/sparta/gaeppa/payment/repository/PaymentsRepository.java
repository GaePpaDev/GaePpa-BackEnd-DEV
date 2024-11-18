package com.sparta.gaeppa.payment.repository;

import com.sparta.gaeppa.order.entity.Orders;
import com.sparta.gaeppa.payment.entity.Payments;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentsRepository extends JpaRepository<Payments, UUID> {
    Payments findPaymentByOrder(Orders order);

    @Query(value = "SELECT p FROM Payments p JOIN Orders po ON p.order = po where po.member.memberId = :memberId")
    List<Payments> findAllPaymentsByMember_MemberId(UUID memberId);
}

