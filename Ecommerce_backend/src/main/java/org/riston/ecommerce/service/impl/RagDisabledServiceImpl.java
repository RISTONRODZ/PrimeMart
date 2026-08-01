package org.riston.ecommerce.service.impl;

import org.riston.ecommerce.service.RagService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@Primary
@ConditionalOnProperty(name = "app.rag.enabled", havingValue = "false")
public class RagDisabledServiceImpl implements RagService {

    @Override
    public Flux<String> query(String userQuestion, String conversationId, String userEmail) {
        return Flux.just("The AI assistant feature is currently unavailable as it runs locally. Please browse our products directly or contact support for assistance.");
    }
}