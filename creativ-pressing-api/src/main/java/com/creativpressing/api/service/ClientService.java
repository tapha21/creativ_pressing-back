package com.creativpressing.api.service;

import com.creativpressing.api.dto.request.ClientRequest;
import com.creativpressing.api.dto.response.ClientResponse;
import com.creativpressing.api.entity.Client;
import com.creativpressing.api.exception.BusinessException;
import com.creativpressing.api.exception.ResourceNotFoundException;
import com.creativpressing.api.mapper.AppMapper;
import com.creativpressing.api.repository.ClientRepository;
import com.creativpressing.api.repository.CustomerOrderRepository;
import com.creativpressing.api.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository repo;
    private final CustomerOrderRepository orderRepo;
    private final ShopService shopService;

    public Client getEntity(UUID id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client introuvable"));
    }

    public List<ClientResponse> findByShop(UUID shopId, String search) {
        List<Client> clients = findClients(shopId, search);

        return clients.stream()
                .map(client -> AppMapper.toClientResponse(client, countOrders(client)))
                .toList();
    }

    public ClientResponse create(ClientRequest request, UUID shopId) {
        shopService.getEntity(shopId);
        checkPhoneIsAvailable(shopId, request.phone());

        Client client = new Client();
        AppMapper.updateClient(client, request);
        client.setShopId(shopId);

        return AppMapper.toClientResponse(repo.save(client), 0);
    }

    public ClientResponse update(UUID id, ClientRequest request) {
        Client client = getEntity(id);
        SecurityUtils.assertShopAccess(client.getShopId());
        AppMapper.updateClient(client, request);

        return AppMapper.toClientResponse(repo.save(client), countOrders(client));
    }

    public void delete(UUID id) {
        Client client = getEntity(id);
        SecurityUtils.assertShopAccess(client.getShopId());

        repo.deleteById(id);
    }

    private List<Client> findClients(UUID shopId, String search) {
        if (search == null || search.isBlank()) {
            return repo.findByShopId(shopId);
        }

        return repo.findByShopIdAndNameContainingIgnoreCase(shopId, search);
    }

    private void checkPhoneIsAvailable(UUID shopId, String phone) {
        if (repo.existsByShopIdAndPhone(shopId, phone)) {
            throw new BusinessException("Ce numero client existe deja dans cette boutique");
        }
    }

    private long countOrders(Client client) {
        return orderRepo.countByClientId(client.getId());
    }
}
