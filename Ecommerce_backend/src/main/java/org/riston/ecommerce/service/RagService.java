package org.riston.ecommerce.service;

import reactor.core.publisher.Flux;

public interface RagService {
    Flux<String> query(String userQuestion, String conversationId, String userEmail);
}
