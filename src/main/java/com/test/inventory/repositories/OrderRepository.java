package com.test.inventory.repositories;

import com.test.inventory.dtos.OrdersByDateAndStore;
import com.test.inventory.dtos.SoldProducts;
import com.test.inventory.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.Tuple;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query(value = "select date(ores.date_time) as orderDate, s.code as storeCode,  " +
            "count(o.id) as numberOfOrders from orders o \n" +
            "inner join stores s  on o.store_id = s.id\n" +
            "inner join order_resume ores on ores.id  = o.order_resume_id " +
            "group by date(ores.date_time), s.code", nativeQuery = true)
    List<Tuple> obtainNumberOfOrdersByDateAndStore();


    @Query(value = "select s.code as storeCode, p.code as productCode, " +
            "sum(o.items) as numberOfSoldProducts from orders o " +
            "inner join stores s on s.id = o.store_id " +
            "inner join products p on o.product_id = p.id " +
            "group by s.code, p.code", nativeQuery = true)
    List<Tuple> obtainNumberOfSoldProductsByStore();
}
