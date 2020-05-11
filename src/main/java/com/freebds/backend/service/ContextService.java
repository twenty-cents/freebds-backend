package com.freebds.backend.service;

import com.freebds.backend.common.web.context.resources.ContextResource;
import com.freebds.backend.model.User;
import org.springframework.stereotype.Service;

@Service
public interface ContextService {

    /**
     * Get current user
     * @return the current user
     */
    User getContext();

    ContextResource getContext(String context, Long libraryId, String roleRequest);

}
