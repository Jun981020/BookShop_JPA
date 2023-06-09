package jpabook.jpashop.repository.query;

import jpabook.jpashop.repository.dto.OrderItemQueryDto;
import jpabook.jpashop.repository.dto.OrderQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDto() {
        List<OrderQueryDto> result = findOrders();
        result.forEach(
                o -> {
                    List<OrderItemQueryDto> list = findOrderItems(o.getOrderId());
                    o.setOrderItems(list);
                }
        );
        return result;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "select new jpabook.jpashop.repository.dto.OrderItemQueryDto(oi.order.id,i.name,oi.orderPrice,oi.count) " +
                        " from OrderItem oi" +
                        " join oi.item i" +
                        " where oi.order.id = :orderId"
                , OrderItemQueryDto.class
        ).setParameter("orderId",orderId).getResultList();

    }

    private List<OrderQueryDto> findOrders() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.dto.OrderQueryDto(o.id,m.name,o.orderDate,o.status,d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d"
                , OrderQueryDto.class
        ).getResultList();
    }
}
