package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.doInit1();
    }

    @Component
    @RequiredArgsConstructor
    @Transactional
    static class InitService{
        private final EntityManager em;

        public void doInit1(){
            Member member1 = getMember("userA","서울","22-12","0123");
            em.persist(member1);
            Member member2 = getMember("userB", "대구", "9912-12", "1234");
            em.persist(member2);

            Book book1 = getBook("JPA1",10000,100);
            em.persist(book1);

            Book book2 = getBook("JPA2", 20000, 100);
            em.persist(book2);

            Book spring1 = getBook("SPRING1", 10000, 100);
            em.persist(spring1);

            Book spring2 = getBook("SPRING2", 30000, 100);
            em.persist(spring2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            OrderItem orderItem3 = OrderItem.createOrderItem(spring1, 10000, 4);
            OrderItem orderItem4 = OrderItem.createOrderItem(spring2, 30000, 3);

            Delivery delivery1 = new Delivery();
            delivery1.setAddress(member1.getAddress());

            Delivery delivery2 = new Delivery();
            delivery2.setAddress(member2.getAddress());

            Order order1 = Order.createOrder(member1, delivery1, orderItem1, orderItem2);
            Order order2 = Order.createOrder(member2, delivery2, orderItem3, orderItem4);

            em.persist(order1);
            em.persist(order2);


        }

        private static Book getBook(String name,int price, int stockQuantity) {
            Book book1 = new Book();
            book1.setName(name);
            book1.setPrice(price);
            book1.setStockQuantity(stockQuantity);
            return book1;
        }

        private static Member getMember(String name,String city,String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city,street,zipcode));
            return member;
        }
    }
}
