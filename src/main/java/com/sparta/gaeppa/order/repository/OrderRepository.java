package com.sparta.gaeppa.order.repository;

import com.sparta.gaeppa.order.entity.Orders;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders, UUID> {
    List<Orders> findAllOrdersByMember_MemberId(UUID memberId);
}
