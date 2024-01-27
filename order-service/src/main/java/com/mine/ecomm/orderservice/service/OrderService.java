package com.mine.ecomm.orderservice.service;

import com.mine.ecomm.orderservice.dto.InventoryResponse;
import com.mine.ecomm.orderservice.dto.OrderLineItemDTO;
import com.mine.ecomm.orderservice.dto.OrderRequest;
import com.mine.ecomm.orderservice.dto.OrderStatus;
import com.mine.ecomm.orderservice.entity.Order;
import com.mine.ecomm.orderservice.entity.OrderLineItem;
import com.mine.ecomm.orderservice.exception.ServiceException;
import com.mine.ecomm.orderservice.repository.OrderItemsRepo;
import com.mine.ecomm.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemsRepo orderItemsRepo;
    private final RestTemplateProvider restTemplateProvider;
    private final WebClient webClient;

    public String placeOrder(final OrderRequest orderRequest) {
        // get order line items
        final List<OrderLineItem> orderLineItems = getAllProductsFromRequest(orderRequest);

        // sku codes of order line items to check in stock
        final List<String> skuCodes = orderLineItems
                    .stream()
                    .map(OrderLineItem::getSkuCode)
                    .toList();

        final boolean allProductsInStock = this.checkingProductsInStock(skuCodes);
        if (allProductsInStock) {
            final Order order = new Order();
            order.setOrderId(UUID.randomUUID().toString());
            final Order createdOrder = orderRepository.saveAndFlush(order);
            // save order line items for the order
            orderLineItems.forEach(orderLineItem -> orderLineItem.setOrder(createdOrder));
            orderItemsRepo.saveAllAndFlush(orderLineItems);
            return createdOrder.getOrderId();
        }
        throw new IllegalArgumentException( "Product is not in stock, please try again later.");
    }

	public List<OrderRequest> getAllBuyerOrders(final String buyerEmail) {
		List<OrderRequest> buyerOrders = new ArrayList<>();
		List<Order> allOrders = orderRepository.findByBuyer(buyerEmail);
		for (Order order : allOrders) {
			final OrderRequest orderRequest = new OrderRequest();
			final List<OrderLineItemDTO> orderLineItemDTOList =
                    order.getOrderLineItemList().stream().map(this::buildOrderItemDto).toList();
			orderRequest.setOrderLineItemDTOList(orderLineItemDTOList);
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
//        order.setStatus(OrderStatus.CANCELLED.getVal());
        orderRepository.saveAndFlush(order);
        return "Your order has been successfully cancelled.";
    }

    public String returnOrderById(final int id) {
        Optional<Order> optOrder = orderRepository.findById(id);
        if (optOrder.isEmpty()) {
            throw new ServiceException("Order is not present with id.");
        }
        final Order order = optOrder.get();
//        order.setStatus(OrderStatus.RETURNED.getVal());
        orderRepository.saveAndFlush(order);
        return "Return request has been created successfully.";
    }

    public boolean rateSeller(final String seller, final int rate) {
        final ResponseEntity<String> response = restTemplateProvider.post(seller, rate);
        return response.getStatusCode() == HttpStatus.OK;
    }

    /**
     * This method checks the availability of products in stock based on the given SKU codes by making a GET request to the Product Service.
     *
     * @param skuCodes The list of SKU codes to check.
     * @return An array of InventoryResponse objects representing the availability of each product.
     */
    private Boolean checkingProductsInStock(final List<String> skuCodes) {
        final InventoryResponse[] inventoryResponse = webClient.get()
                .uri("http://localhost:8083/api/product/in-stock",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();
        if (inventoryResponse != null && inventoryResponse.length != 0) {
            // Check if all products are in stock
            return Arrays.stream(inventoryResponse).allMatch(InventoryResponse::isInStock);
        }
        return false;
    }

    private static OrderLineItem convertToEntity(OrderLineItemDTO orderLineItemDTO) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setProductPrice(orderLineItemDTO.getProductPrice());
        orderLineItem.setQuantity(orderLineItemDTO.getQuantity());
        orderLineItem.setSkuCode(orderLineItemDTO.getSkuCode());
        return orderLineItem;
    }

    private static List<OrderLineItem> getAllProductsFromRequest(final OrderRequest orderRequest) {
       return orderRequest.getOrderLineItemDTOList().stream()
               .map(OrderService::convertToEntity)
               .toList();
    }

    private OrderLineItemDTO buildOrderItemDto(OrderLineItem orderLineItem) {
        return OrderLineItemDTO.builder()
                .skuCode(orderLineItem.getSkuCode())
                .productName(orderLineItem.getProductName())
                .productPrice(orderLineItem.getProductPrice())
                .quantity(orderLineItem.getQuantity())
                .build();
    }
}
