package org.riston.ecommerce.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Endpoints to chat with ai")
public class AiController {

    private final RagService ragService;

    @Operation(summary = "chatbot", description = "Get context based Responses")
    @GetMapping(value = "/search")
    public Flux<String> search(@RequestParam("q") String q,
                               Authentication authentication,
                               HttpSession session) {

        String conversationId;
        String userEmail = null;

        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getName())) {
            userEmail = authentication.getName();
            conversationId = UUID.nameUUIDFromBytes(userEmail.getBytes()).toString();
        } else {
            String guestId = (String) session.getAttribute("GUEST_CONVERSATION_ID");
            if (guestId == null) {
                guestId = UUID.randomUUID().toString();
                session.setAttribute("GUEST_CONVERSATION_ID", guestId);
            }
            conversationId = guestId;
        }

        return ragService.query(q, conversationId, userEmail);
    }
}