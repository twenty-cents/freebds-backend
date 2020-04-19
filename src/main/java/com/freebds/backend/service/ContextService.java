package com.freebds.backend.service;

import com.freebds.backend.common.web.resources.ContextResource;
import org.springframework.stereotype.Service;

@Service
public interface ContextService {

    ContextResource getContext(String context, Long libraryId, String roleRequest);

}
