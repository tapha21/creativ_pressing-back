package com.creativpressing.api.security;

import com.creativpressing.api.enums.EmployeeRole;
import com.creativpressing.api.exception.BusinessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.UUID;

public final class SecurityUtils {
    private SecurityUtils() {
    }

    public static AuthPrincipal currentPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof AuthPrincipal principal)) {
            throw new AccessDeniedException("Non authentifié");
        }
        return principal;
    }

    public static UUID requireShopId() {
        AuthPrincipal principal = currentPrincipal();
        if (principal.shopId() == null) {
            throw new AccessDeniedException("Aucune boutique associée à ce compte");
        }
        return principal.shopId();
    }

    public static boolean isAdmin() {
        return currentPrincipal().role() == EmployeeRole.ADMIN;
    }

    /**
     * Pour un OWNER/EMPLOYEE, force le shopId de sa propre boutique quel que soit ce que
     * le client a envoyé. Un ADMIN doit fournir explicitement le shopId visé.
     */
    public static UUID resolveShopId(UUID requested) {
        AuthPrincipal principal = currentPrincipal();
        if (principal.role() == EmployeeRole.ADMIN) {
            if (requested == null) {
                throw new BusinessException("shopId requis pour un administrateur");
            }
            return requested;
        }
        return requireShopId();
    }

    /** Interdit l'accès à une ressource qui n'appartient pas à la boutique de l'utilisateur (sauf ADMIN). */
    public static void assertShopAccess(UUID entityShopId) {
        AuthPrincipal principal = currentPrincipal();
        if (principal.role() == EmployeeRole.ADMIN) {
            return;
        }
        if (!Objects.equals(principal.shopId(), entityShopId)) {
            throw new AccessDeniedException("Accès refusé à cette ressource");
        }
    }
}
