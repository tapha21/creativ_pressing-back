package com.creativpressing.api.service;

import com.creativpressing.api.dto.request.PhotoItemRequest;
import com.creativpressing.api.dto.response.PhotoItemResponse;
import com.creativpressing.api.entity.PhotoItem;
import com.creativpressing.api.enums.PhotoType;
import com.creativpressing.api.exception.ResourceNotFoundException;
import com.creativpressing.api.mapper.AppMapper;
import com.creativpressing.api.repository.PhotoItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PhotoService {
    private final PhotoItemRepository repo;
    private final OrderService orderService;
    private final MediaStorageService mediaStorageService;

    public List<PhotoItemResponse> findByOrder(UUID orderId) {
        return repo.findByOrderId(orderId)
                .stream()
                .map(AppMapper::toPhotoItemResponse)
                .toList();
    }

    public List<PhotoItemResponse> findByShop(UUID shopId) {
        return repo.findByOrderShopId(shopId)
                .stream()
                .map(AppMapper::toPhotoItemResponse)
                .toList();
    }

    public PhotoItemResponse create(PhotoItemRequest request) {
        PhotoItem photo = new PhotoItem();
        photo.setOrder(orderService.getEntity(request.orderId()));
        photo.setType(request.type());
        photo.setUrl(request.url());
        photo.setDate(request.date());

        return AppMapper.toPhotoItemResponse(repo.save(photo));
    }

    public PhotoItemResponse upload(UUID orderId, PhotoType type, LocalDate date, MultipartFile file) {
        String fileUrl = mediaStorageService.save(file);

        PhotoItem photo = new PhotoItem();
        photo.setOrder(orderService.getEntity(orderId));
        photo.setType(type);
        photo.setUrl(fileUrl);
        photo.setDate(date == null ? LocalDate.now() : date);

        return AppMapper.toPhotoItemResponse(repo.save(photo));
    }

    public void delete(UUID id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("Photo introuvable");
        }

        repo.deleteById(id);
    }
}
