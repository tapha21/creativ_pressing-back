package com.creativpressing.api.service;

import com.creativpressing.api.dto.request.ShopRequest;
import com.creativpressing.api.dto.response.ShopResponse;
import com.creativpressing.api.entity.PressingShop;
import com.creativpressing.api.exception.BusinessException;
import com.creativpressing.api.exception.ResourceNotFoundException;
import com.creativpressing.api.mapper.AppMapper;
import com.creativpressing.api.repository.PressingShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ShopService {
    private final PressingShopRepository repo;

    public List<ShopResponse> findAll() {
        return repo.findAll()
                .stream()
                .map(AppMapper::toShopResponse)
                .toList();
    }

    public PressingShop getEntity(UUID id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Boutique introuvable"));
    }

    public ShopResponse findById(UUID id) {
        return AppMapper.toShopResponse(getEntity(id));
    }

    public ShopResponse create(ShopRequest request) {
        if (repo.existsByEmailIgnoreCase(request.email())) {
            throw new BusinessException("Cet email est deja utilise");
        }

        PressingShop shop = new PressingShop();
        AppMapper.updateShop(shop, request);
        shop.setActive(true);

        return AppMapper.toShopResponse(repo.save(shop));
    }

    public ShopResponse update(UUID id, ShopRequest request) {
        PressingShop shop = getEntity(id);
        AppMapper.updateShop(shop, request);

        return AppMapper.toShopResponse(repo.save(shop));
    }
}
