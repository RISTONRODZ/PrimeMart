package org.riston.ecommerce.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.riston.ecommerce.service.RagService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.UUID;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
@Slf4j
public class AiController {

    private final RagService ragService;

    @GetMapping(value = "/search")
    public Flux<String> search(@RequestParam("q") String q,
                               Authentication authentication,
                               HttpSession session) {

        String conversationId;

        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            conversationId = UUID.nameUUIDFromBytes(authentication.getName().getBytes()).toString();
        } else {
            String guestId = (String) session.getAttribute("GUEST_CONVERSATION_ID");
            if (guestId == null) {
                guestId = UUID.randomUUID().toString();
                session.setAttribute("GUEST_CONVERSATION_ID", guestId);
            }
            conversationId = guestId;
        }

        return ragService.query(q, conversationId);
    }
}