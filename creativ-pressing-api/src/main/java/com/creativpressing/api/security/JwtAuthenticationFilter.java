package com.creativpressing.api.security;

import com.creativpressing.api.entity.Employee;
import com.creativpressing.api.entity.PressingShop;
import com.creativpressing.api.repository.EmployeeRepository;
import com.creativpressing.api.repository.PressingShopRepository;
import com.creativpressing.api.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final EmployeeRepository employeeRepo;
    private final PressingShopRepository shopRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            Optional<Claims> claims = jwtService.parse(token);

            if (claims.isPresent()) {
                UUID employeeId = JwtService.subjectAsUuid(claims.get());
                Optional<Employee> employeeOpt = employeeRepo.findById(employeeId);

                if (employeeOpt.isPresent() && Boolean.TRUE.equals(employeeOpt.get().getActive())) {
                    Employee employee = employeeOpt.get();
                    boolean shopUsable = true;

                    if (employee.getShopId() != null) {
                        Optional<PressingShop> shop = shopRepo.findById(employee.getShopId());
                        shopUsable = shop.isPresent() && Boolean.TRUE.equals(shop.get().getActive());
                    }

                    if (shopUsable) {
                        AuthPrincipal principal = new AuthPrincipal(employee.getId(), employee.getShopId(),
                                employee.getRole(), employee.getEmail(), employee.getName());
                        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + employee.getRole().name()));
                        var authentication = new UsernamePasswordAuthenticationToken(principal, null, authorities);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        }

        chain.doFilter(request, response);
    }
}
