package com.creativpressing.api.service;

import com.creativpressing.api.dto.request.OrderRequest;
import com.creativpressing.api.dto.response.OrderResponse;
import com.creativpressing.api.entity.Client;
import com.creativpressing.api.entity.CustomerOrder;
import com.creativpressing.api.enums.OrderStatus;
import com.creativpressing.api.enums.PaymentStatus;
import com.creativpressing.api.exception.ResourceNotFoundException;
import com.creativpressing.api.mapper.AppMapper;
import com.creativpressing.api.repository.ClientRepository;
import com.creativpressing.api.repository.CustomerOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final CustomerOrderRepository repo;
    private final ClientRepository clientRepo;
    private final ShopService shopService;

    public CustomerOrder getEntity(UUID id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commande introuvable"));
    }

    public List<OrderResponse> findByShop(UUID shopId, OrderStatus status, PaymentStatus payment) {
        List<CustomerOrder> orders = findOrders(shopId, status, payment);

        return orders.stream()
                .map(AppMapper::toOrderResponse)
                .toList();
    }

    public OrderResponse create(OrderRequest request) {
        CustomerOrder order = new CustomerOrder();
        order.setShop(shopService.getEntity(request.shopId()));
        attachClient(order, request.clientId());
        AppMapper.updateOrder(order, request);

        return AppMapper.toOrderResponse(repo.save(order));
    }

    public OrderResponse update(UUID id, OrderRequest request) {
        CustomerOrder order = getEntity(id);
        attachClient(order, request.clientId());
        AppMapper.updateOrder(order, request);

        return AppMapper.toOrderResponse(repo.save(order));
    }

    public OrderResponse updateStatus(UUID id, OrderStatus status) {
        CustomerOrder order = getEntity(id);
        order.setStatus(status);

        return AppMapper.toOrderResponse(repo.save(order));
    }

    public void delete(UUID id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Commande introuvable");
        }

        repo.deleteById(id);
    }

    private List<CustomerOrder> findOrders(UUID shopId, OrderStatus status, PaymentStatus payment) {
        if (status != null) {
            return repo.findByShopIdAndStatus(shopId, status);
        }

        if (payment != null) {
            return repo.findByShopIdAndPayment(shopId, payment);
        }

        return repo.findByShopId(shopId);
    }

    private void attachClient(CustomerOrder order, UUID clientId) {
        if (clientId == null) {
            return;
        }

        Client client = clientRepo.findById(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable"));

        order.setClient(client);
        order.setClientName(client.getName());
        order.setClientPhone(client.getPhone());
    }
}
