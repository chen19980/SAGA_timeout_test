package com.booking.bookingservice.service;

import com.booking.bookingservice.model.OrderDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
@Data
@Slf4j
public class OrderManagerService {

    final Map<String, OrderDto> orders = new HashMap<>();
    final Map<String, String> orderStatusMap = new HashMap<>();

    public void newOrder(Exchange exchange) {

        System.out.println("@@@ newOrder service start " + Instant.now().toString());

        try {
            Thread.sleep(6000);
        } catch (Exception e) {
        }

        OrderDto order = exchange.getMessage().getBody(OrderDto.class);
        String id = exchange.getIn().getHeader("id", String.class);
        orders.put(id, order);
        log.info("Persisted Order. ID: [" + id + "] [" + order + "]");

        System.out.println("@@@ newOrder service end " + Instant.now().toString());
        System.out.println("@@@ newOrder finish");
    }

    public void cancelOrder(Exchange exchange) {

        System.out.println("@@@ cancelOrder service start " + Instant.now().toString());

        String id = exchange.getIn().getHeader("id", String.class);
        log.info("Cancelling Order. ID: [" + id + "]");
//        orders.remove(id);

        System.out.println("@@@ cancelOrder service end " + Instant.now().toString());
        System.out.println("@@@ cancelOrder finish");
    }

    public void shipOrder(Exchange exchange) {

        System.out.println("@@@ shipOrder service start " + Instant.now().toString());

        String id = exchange.getIn().getHeader("id", String.class);
        OrderDto order = orders.get(id);
        log.info("Preparing to ship Order. ID: [" + order + "]");
        if (order.getQuantity() > 10) {
            throw new RuntimeException("Too many items to ship. Can't ship.");
        }
        log.info("Shipped Order. ID: [" + orders.get(id) + "]");

        System.out.println("@@@ shipOrder service end " + Instant.now().toString());
        System.out.println("@@@ shipOrder finish");
    }

    public void cancelShipping(Exchange exchange) {

        System.out.println("@@@ cancelShipping service start " + Instant.now().toString());

        String id = exchange.getIn().getHeader("id", String.class);
        OrderDto order = orders.get(id);
        log.info("Cancelling ship Order. ID: [" + order + "]");
        orderStatusMap.put(id, "Cancelled");
        log.info("Cancelled Shipping Order. ID: [" + orders.get(id) + "]");

        System.out.println("@@@ cancelShipping service end " + Instant.now().toString());
        System.out.println("@@@ cancelShipping finish");
    }

    public void completeShipping(Exchange exchange) {

        System.out.println("@@@ completeShipping service start " + Instant.now().toString());

        String id = exchange.getIn().getHeader("id", String.class);
        OrderDto order = orders.get(id);
        log.info("Completing shipping Order. ID: [" + order + "]");
        orderStatusMap.put(id, "Shipped");

        System.out.println("@@@ completeShipping service end " + Instant.now().toString());
        System.out.println("@@@ completeShipping finish");
    }
}
