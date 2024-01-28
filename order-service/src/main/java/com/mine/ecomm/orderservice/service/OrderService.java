package com.mine.ecomm.orderservice.service;

import java.time.LocalDateTime;
import java.util.*;

import com.mine.ecomm.orderservice.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.mine.ecomm.orderservice.entity.OrderEntity;
import com.mine.ecomm.orderservice.entity.OrderLineItem;
import com.mine.ecomm.orderservice.entity.OrderLineItemId;
import com.mine.ecomm.orderservice.exception.ServiceException;
import com.mine.ecomm.orderservice.repository.OrderItemsRepo;
import com.mine.ecomm.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemsRepo orderItemsRepo;
    private final RestTemplateProvider restTemplateProvider;
    private final WebClient webClient;

    public Optional<String> placeOrder(final OrderDTO orderRequest) {
        // get order line items
        final List<OrderLineItem> orderLineItems = getAllProductsFromRequest(orderRequest);

        // sku codes of order line items to check in stock
        final List<String> skuCodes = orderLineItems
                    .stream()
                    .map(OrderLineItem::getSkuCode)
                    .toList();

        final boolean allProductsInStock = this.checkingProductsInStock(skuCodes);
        if (allProductsInStock) {
            final OrderEntity orderEntity = new OrderEntity();
            orderEntity.setOrderId(UUID.randomUUID().toString());
            orderEntity.setBuyerEmail(orderRequest.getBuyerEmail());
            final OrderEntity createdOrderEntity = orderRepository.saveAndFlush(orderEntity);
            // save orderEntity line items for the orderEntity
            orderLineItems.forEach(orderLineItem -> {
                orderLineItem.setOrderEntity(createdOrderEntity);
                orderLineItem.setOrderedOn(LocalDateTime.now());
                orderLineItem.setStatus(OrderStatus.OPEN.getVal());
            });
            orderItemsRepo.saveAllAndFlush(orderLineItems);
            return Optional.of(createdOrderEntity.getOrderId());
        }
        return Optional.empty();
    }

	public List<OrderDTO> getAllBuyerOrders(final String buyerEmail) {
		final List<OrderDTO> buyerOrders = new ArrayList<>();
		final List<OrderEntity> allOrderEntities = orderRepository.findByBuyerEmailOrderByOrderedOnDesc(buyerEmail);
		for (OrderEntity orderEntity : allOrderEntities) {
            final List<OrderLineItem> orderLineItems = orderItemsRepo.findByOrderEntity(orderEntity);
            final List<OrderLineItemDTO> orderLineItemsResponse =
                    orderLineItems.stream().map(OrderService::buildOrderItemResponse).toList();
			final OrderDTO orderResponse = new OrderDTO();
			orderResponse.setOrderLineItemDTOList(orderLineItemsResponse);
			buyerOrders.add(orderResponse);
		}
		return buyerOrders;
	}

    public String cancelOrderById(final OrderItemIdRequest orderItemIdRequest) {
        final OrderLineItemId orderLineItemId = getOrderOrderLineItemId(orderItemIdRequest);
        final Optional<OrderLineItem> optOrderLineItem = orderItemsRepo.findById(orderLineItemId);
        if (optOrderLineItem.isEmpty()) {
            throw new ServiceException("OrderEntity is not present with id.");
        }
        final OrderLineItem orderLineItem = optOrderLineItem.get();
        orderLineItem.setStatus(OrderStatus.CANCELLED.getVal());
        orderItemsRepo.saveAndFlush(orderLineItem);
        return "Your orderEntity has been successfully cancelled.";
    }

    public String returnOrderById(final OrderItemIdRequest orderItemIdRequest) {
        final OrderLineItemId orderLineItemId = getOrderOrderLineItemId(orderItemIdRequest);
        final Optional<OrderLineItem> optOrderLineItem = orderItemsRepo.findById(orderLineItemId);
        if (optOrderLineItem.isEmpty()) {
            throw new ServiceException("OrderEntity is not present with id.");
        }
        final OrderLineItem orderLineItem = optOrderLineItem.get();
        orderLineItem.setStatus(OrderStatus.RETURNED.getVal());
        orderItemsRepo.saveAndFlush(orderLineItem);
        return "Return request has been created successfully.";
    }

    private static OrderLineItemId getOrderOrderLineItemId(OrderItemIdRequest orderItemIdRequest) {
        return new OrderLineItemId(orderItemIdRequest.getSkuCode(), orderItemIdRequest.getOrderId());
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

    private static List<OrderLineItem> getAllProductsFromRequest(final OrderDTO orderRequest) {
       return orderRequest.getOrderLineItemDTOList().stream()
               .map(OrderService::convertToEntity)
               .toList();
    }

    private static OrderLineItem convertToEntity(OrderLineItemDTO orderLineItemDTO) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setProductName(orderLineItemDTO.getProductName());
        orderLineItem.setProductPrice(orderLineItemDTO.getProductPrice());
        orderLineItem.setQuantity(orderLineItemDTO.getQuantity());
        orderLineItem.setSkuCode(orderLineItemDTO.getSkuCode());
        return orderLineItem;
    }

    /**
     * This method builds an OrderLineItemDTO object based on the given OrderLineItem.
     *
     * @param orderLineItem The OrderLineItem object to convert.
     * @return The corresponding OrderLineItemDTO object.
     */
    private static OrderLineItemDTO buildOrderItemResponse(OrderLineItem orderLineItem) {
        return OrderLineItemDTO.builder()
                .skuCode(orderLineItem.getSkuCode())
                .productName(orderLineItem.getProductName())
                .productPrice(orderLineItem.getProductPrice())
                .quantity(orderLineItem.getQuantity())
                .status(OrderStatus.valToStatus(orderLineItem.getStatus()))
                .sellerEmail(orderLineItem.getSellerEmail())
                .orderedOn(orderLineItem.getOrderedOn())
                .deliveredOn(orderLineItem.getDeliveredOn())
                .cancelledOn(orderLineItem.getCancelledOn())
                .returnedOn(orderLineItem.getReturnedOn())
                .build();
    }
}
