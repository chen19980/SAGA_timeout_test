package com.booking.bookingservice.controller;

import com.booking.bookingservice.service.CreditService;
import com.booking.bookingservice.service.OrderManagerService;
import com.booking.bookingservice.model.OrderDto;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.apache.camel.model.rest.RestParamType.body;

@Component
public class OrderController extends RouteBuilder {

    private final OrderManagerService orderManagerService;
    private final CreditService creditService;

    public OrderController(OrderManagerService orderManagerService, CreditService creditService) {
        this.orderManagerService = orderManagerService;
        this.creditService = creditService;
    }

    @Override
    public void configure() throws Exception {
        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true")
                .enableCORS(true)
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "User API")
                .apiProperty("api.version", "1.0.0");

        onException(Exception.class)
                .handled(true)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
                .setHeader(Exchange.CONTENT_TYPE, constant("text/json"))
                .setBody().simple("${exception.message}");

        rest()
                .consumes("application/json").produces("application/json")
                .post("/orders")
                .responseMessage("201", "When Created")
                .description("Create a new order").type(OrderDto.class)
                .param().name("body").type(body).description("Payload for an Order")
                .endParam()
                .to("direct:order")
        ;

        rest()
                .produces("application/json")
                .get("/databases")
                .responseMessage("200", "All Data")
                .to("direct:databases");

        from("direct:databases")
                .setBody(e -> {
                    Map<String, Object> databases = new LinkedHashMap<>();
                    databases.put("Customer Account", creditService.getCustomerAccount());
                    databases.put("Order Amount", creditService.getOrderAmount());
                    databases.put("Order Status", orderManagerService.getOrderStatusMap());
                    databases.put("Z Orders", orderManagerService.getOrders());
                    return databases;
                });
    }
}
