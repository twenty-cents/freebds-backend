package com.freebds.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.freebds.backend.common.web.context.resources.ContextResource;
import com.freebds.backend.common.web.graphicNovel.requests.BarcodeScanRequest;
import com.freebds.backend.common.web.graphicNovel.resources.GraphicNovelResource;
import com.freebds.backend.model.Library;
import com.freebds.backend.model.User;
import com.freebds.backend.security.jwt.AuthEntryPointJwt;
import com.freebds.backend.security.jwt.JwtUtils;
import com.freebds.backend.security.services.UserDetailsServiceImpl;
import com.freebds.backend.service.ContextService;
import com.freebds.backend.service.GraphicNovelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GraphicNovelController.class)
@AutoConfigureMockMvc(addFilters = false)
public class GraphicNovelControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private GraphicNovelController graphicNovelController;

    @MockBean
    GraphicNovelService graphicNovelService;

    @MockBean
    UserDetailsServiceImpl userDetailsService;

    @MockBean
    AuthEntryPointJwt unauthorizedHandler;

    @MockBean
    JwtUtils jwtUtils;

    @MockBean
    ContextService contextService;

    private User user;
    private Library library;
    private ContextResource contextResource;

    @BeforeEach
    public void setUp() {

        mockMvc = MockMvcBuilders
                .standaloneSetup (graphicNovelController)
                .build ( );

        user = new User();
        user.setId(1L);

        library = Library
                .builder()
                .id(1L)
                .build();

        contextResource = ContextResource
                .builder()
                .user(user)
                .library(library)
                .userRole("ADMIN")
                .isValid(true)
                .build();
    }

    @DisplayName("Scan should be ok - Valid request values.")
    @Test
    void scan1() throws Exception {
        BarcodeScanRequest barcodeScanRequest = BarcodeScanRequest
                .builder()
                .libraryId(1L)
                .build();

        List<GraphicNovelResource> graphicNovelResources = new ArrayList<GraphicNovelResource>();

        given (graphicNovelService.scan (contextResource, barcodeScanRequest)).willReturn (graphicNovelResources);

        mockMvc.perform( MockMvcRequestBuilders
                .post("/api/graphic-novels/scan")
                .content(asJsonString(barcodeScanRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @DisplayName("Scan should be ko - Invalid request values.")
    @Test
    void scan2() throws Exception {
        BarcodeScanRequest barcodeScanRequest = BarcodeScanRequest
                .builder()
                .libraryId(1L)
                .build();

        List<GraphicNovelResource> graphicNovelResources = new ArrayList<GraphicNovelResource>();

        given (graphicNovelService.scan (contextResource, barcodeScanRequest)).willReturn (graphicNovelResources);

        mockMvc.perform( MockMvcRequestBuilders
                .post("/api/graphic-novels/scan")
                .content(asJsonString(null))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper( ).writeValueAsString (obj);
        } catch (Exception e) {
            throw new RuntimeException (e);
        }
    }

}