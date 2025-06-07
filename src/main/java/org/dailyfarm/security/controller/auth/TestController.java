package org.dailyfarm.security.controller.auth;

import org.dailyfarm.security.service.auth.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/public")
    public String testPublic() {
        return "Public endpoint - доступен всем";
    }

    @GetMapping("/protected")
    public String testProtected() {
        return "Protected endpoint - требует авторизации";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String testAdmin() {
        return "Admin endpoint - только для админов";
    }

    @GetMapping("/user-info")
    public ResponseEntity<String> getUserInfo(JwtService auth) {
        if (auth == null) {
            return ResponseEntity.ok("Не авторизован");
        }
        return ResponseEntity.ok("Пользователь: " + auth.getCurrentUsername() +
                ", Роли: " + auth.getRolesUser());
    }
}
