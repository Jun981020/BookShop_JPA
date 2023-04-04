package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.dto.OrderQueryDto;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.query.OrderQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    @GetMapping("api/v1/orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();

            order.getOrderItems()
                    .stream().forEach(
                            o -> {
                                o.getItem().getName();
                            }
                    );
        }
        return all;
    }
    @GetMapping("api/v2/orders")
    public List<OrderDto> ordersV2(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        return all.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }
    @GetMapping("api/v3/orders")
    public List<OrderDto> ordersV3(){
        return orderRepository.findAllWithOrderItems()
                .stream().map(OrderDto::new)
                .collect(Collectors.toList());
    }
    @GetMapping("api/v3.1/orders")
    public List<OrderDto> ordersV3_paging(
            @RequestParam(value = "offset",defaultValue = "0")int offset,
            @RequestParam(value = "limit")int limit
    ){
        List<Order> orders = orderRepository.findAllWithMemberAndDelivery(offset, limit);

        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return collect;
    }
    @GetMapping("api/v4/orders")
    public List<OrderQueryDto> ordersV4(){
        return orderQueryRepository.findOrderQueryDto();
    }
    @Data
    static class OrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order){
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems()
                    .stream().map(OrderItemDto::new)
                    .collect(Collectors.toList());
        }
    }
    @Data
    static class OrderItemDto{
        private String name;
        private int count;
        private int price;
        private int totalPrice;

        public OrderItemDto(OrderItem orderItem){
            name = orderItem.getItem().getName();
            count = orderItem.getCount();
            price = orderItem.getOrderPrice();
            totalPrice = count * price;
        }
    }

}
