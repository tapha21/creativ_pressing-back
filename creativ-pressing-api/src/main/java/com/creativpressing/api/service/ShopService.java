package com.creativpressing.api.service;

import com.creativpressing.api.dto.request.ShopActiveRequest;
import com.creativpressing.api.dto.request.ShopRequest;
import com.creativpressing.api.dto.request.ShopSubscriptionRequest;
import com.creativpressing.api.dto.response.ShopResponse;
import com.creativpressing.api.entity.PressingShop;
import com.creativpressing.api.enums.SubscriptionStatus;
import com.creativpressing.api.exception.BusinessException;
import com.creativpressing.api.exception.ResourceNotFoundException;
import com.creativpressing.api.mapper.AppMapper;
import com.creativpressing.api.repository.PressingShopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShopService {
    private final PressingShopRepository repo;
    private final PasswordEncoder passwordEncoder;

    public List<ShopResponse> findAll(String status) {
        List<PressingShop> shops = repo.findAll();

        return shops.stream()
                .filter(shop -> status == null || status.isBlank()
                        || AppMapper.statusLabel(shop).equalsIgnoreCase(status)
                        || (shop.getSubscriptionStatus() != null && shop.getSubscriptionStatus().name().equalsIgnoreCase(status)))
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
        if (request.password() != null && !request.password().isBlank()) {
            shop.setPasswordHash(passwordEncoder.encode(request.password()));
        }

        return AppMapper.toShopResponse(repo.save(shop));
    }

    public ShopResponse update(UUID id, ShopRequest request) {
        PressingShop shop = getEntity(id);
        AppMapper.updateShop(shop, request);
        if (request.password() != null && !request.password().isBlank()) {
            shop.setPasswordHash(passwordEncoder.encode(request.password()));
        }

        return AppMapper.toShopResponse(repo.save(shop));
    }

    public ShopResponse updateSubscription(UUID id, ShopSubscriptionRequest request) {
        PressingShop shop = getEntity(id);
        AppMapper.updateShopSubscription(shop, request);

        return AppMapper.toShopResponse(repo.save(shop));
    }

    public ShopResponse setActive(UUID id, ShopActiveRequest request) {
        PressingShop shop = getEntity(id);
        shop.setActive(request.active());

        return AppMapper.toShopResponse(repo.save(shop));
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void expireOutdatedSubscriptions() {
        LocalDate today = LocalDate.now();
        List<PressingShop> shops = repo.findAll();
        int expired = 0;

        for (PressingShop shop : shops) {
            boolean trialExpired = shop.getSubscriptionStatus() == SubscriptionStatus.TRIAL
                    && shop.getTrialEndsAt() != null && shop.getTrialEndsAt().isBefore(today);
            boolean subscriptionExpired = shop.getSubscriptionStatus() == SubscriptionStatus.ACTIVE
                    && shop.getSubscriptionEndsAt() != null && shop.getSubscriptionEndsAt().isBefore(today);

            if (trialExpired || subscriptionExpired) {
                shop.setSubscriptionStatus(SubscriptionStatus.EXPIRED);
                repo.save(shop);
                expired++;
            }
        }

        if (expired > 0) {
            log.info("{} boutique(s) passee(s) en Expire (essai/abonnement termine)", expired);
        }
    }
}
