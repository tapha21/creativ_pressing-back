package com.creativpressing.api.dto.request;

import jakarta.validation.constraints.NotNull;

public record ShopActiveRequest(@NotNull Boolean active) {
}
