package com.mine.ecomm.orderservice.service;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.mine.ecomm.orderservice.dto.InventoryResponse;
import com.mine.ecomm.orderservice.dto.OrderLineItemsDto;
import com.mine.ecomm.orderservice.dto.OrderRequest;
import com.mine.ecomm.orderservice.dto.OrderStatus;
import com.mine.ecomm.orderservice.entity.Order;
import com.mine.ecomm.orderservice.entity.OrderLineItems;
import com.mine.ecomm.orderservice.exception.ServiceException;
import com.mine.ecomm.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplateProvider restTemplateProvider;
    private final WebClient webClient;

    public void placeOrder(final OrderRequest orderRequest) {
        final Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        final List<OrderLineItems> orderLineItems =
                orderRequest.getOrderLineItemsDtoList()
                            .stream()
                            .map(this::convertToEntity)
                            .toList();
        order.setOrderLineItemsList(orderLineItems);

        final List<String> skuCodes =
                order.getOrderLineItemsList()
                    .stream()
                    .map(OrderLineItems::getSkuCode)
                    .toList();

        // Call Inventory Service, and place order if product is in
        // stock
        final InventoryResponse[] inventoryResponseArray = webClient.get()
                .uri("http://localhost:8083/api/product/in-stock",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        if (inventoryResponseArray != null && inventoryResponseArray.length != 0) {
            final boolean allProductsInStock =
                    Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);
            if (allProductsInStock) {
                orderRepository.saveAndFlush(order);
                return;
            }
        }
        throw new IllegalArgumentException( "Product is not in stock, please try again later.");
    }

	public List<OrderRequest> getAllBuyerOrders(final String buyerEmail) {
		List<OrderRequest> buyerOrders = new ArrayList<>();
		List<Order> allOrders = orderRepository.findByBuyer(buyerEmail);
		for (Order order : allOrders) {
			final OrderRequest orderRequest = new OrderRequest();
			final List<OrderLineItemsDto> orderLineItemsDtoList =
                    order.getOrderLineItemsList().stream().map(this::convertToDto).toList();
			orderRequest.setOrderLineItemsDtoList(orderLineItemsDtoList);
			buyerOrders.add(orderRequest);
		}
		return buyerOrders;
	}

    public String cancelOrderById(final int id) {
        Optional<Order> optOrder = orderRepository.findById(id);
        if (optOrder.isEmpty()) {
            throw new ServiceException("Order is not present with id.");
        }
        final Order order = optOrder.get();
        order.setStatus(OrderStatus.CANCELLED.getVal());
        orderRepository.saveAndFlush(order);
        return "Your order has been successfully cancelled.";
    }

    public String returnOrderById(final int id) {
        Optional<Order> optOrder = orderRepository.findById(id);
        if (optOrder.isEmpty()) {
            throw new ServiceException("Order is not present with id.");
        }
        final Order order = optOrder.get();
        order.setStatus(OrderStatus.RETURNED.getVal());
        orderRepository.saveAndFlush(order);
        return "Return request has been created successfully.";
    }

    public boolean rateSeller(final String seller, final int rate) {
        final ResponseEntity<String> response = restTemplateProvider.post(seller, rate);
        return response.getStatusCode() == HttpStatus.OK;
    }

    private OrderLineItems convertToEntity(OrderLineItemsDto orderLineItemsDto) {
        final OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }

    private OrderLineItemsDto convertToDto(OrderLineItems orderLineItems) {
        final OrderLineItemsDto orderLineItemsDto = new OrderLineItemsDto();
        orderLineItemsDto.setPrice(orderLineItems.getPrice());
        orderLineItemsDto.setQuantity(orderLineItems.getQuantity());
        orderLineItemsDto.setSkuCode(orderLineItems.getSkuCode());
        return orderLineItemsDto;
    }
}
