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
public class CreditService {

    final Map<String, Double> customerAccount = new HashMap<>();
    final Map<String, Double> orderAmount = new HashMap<>();

    public CreditService() {
        customerAccount.put("1000", 100D);
        customerAccount.put("2000", 200D);
        customerAccount.put("3000", 10.00);
    }

    public void makePayment(Exchange exchange){

        System.out.println("@@@ makePayment service start " + Instant.now().toString());

        try {
            Thread.sleep(6000);
        } catch (Exception e) {
        }

        OrderDto order = exchange.getMessage().getBody(OrderDto.class);
        String id = exchange.getIn().getHeader("id", String.class);
        double currentBalance = customerAccount.get(order.getCustomerId());
        log.info("Taking Payment for Order ID: [" + id + "] " +
                "Customer [Id:" + order.getCustomerId() + "] [Current Balance: " + currentBalance + "] [Deduct: " + order.getAmount() + "]");
        if(currentBalance < order.getAmount()) {
            throw new RuntimeException("Can't reserve. Insufficient Credit Balance.");
        }

        customerAccount.put(order.getCustomerId(), currentBalance - order.getAmount());
        orderAmount.put(id, order.getAmount());
        log.info("Payment made for Order ID: [" + id + "] " +
                "Customer [Id:" + order.getCustomerId() + "] [Current Balance: " + customerAccount.get(order.getCustomerId()) + "]");

        System.out.println("@@@ makePayment service end " + Instant.now().toString());
        System.out.println("@@@ makePayment finish");
    }

    public void refundPayment(Exchange exchange){

        System.out.println("@@@ refundPayment service start " + Instant.now().toString());

        String id = exchange.getIn().getHeader("id", String.class);
        String customerId = exchange.getIn().getHeader("customerId", String.class);

        double currentBalance = customerAccount.get(customerId);

        log.info("Returning Payment for Order ID: [" + id + "] " +
                "Customer [Id:" + customerId + "] [Current Balance: " + currentBalance + "] [Refund: " + orderAmount.get(id) + "]");

        customerAccount.put(customerId, currentBalance + orderAmount.get(id));
        log.info("Payment returned for Order ID: [" + id + "] " +
                "Customer [Id:" + customerId + "] [Current Balance: " + customerAccount.get(customerId) + "]");

        System.out.println("@@@ refundPayment service end " + Instant.now().toString());
        System.out.println("@@@ refundPayment finish");
    }
}
