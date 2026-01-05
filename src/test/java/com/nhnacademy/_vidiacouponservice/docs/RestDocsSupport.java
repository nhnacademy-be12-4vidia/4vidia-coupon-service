package com.nhnacademy._vidiacouponservice.docs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith({RestDocumentationExtension.class})
@AutoConfigureRestDocs
public abstract class RestDocsSupport {

    @Autowired
    protected ObjectMapper objectMapper;

    protected MockMvc mockMvc;

    protected RestDocumentationResultHandler restDocs =
            document("{class-name}/{method-name}");

    protected abstract Object initController();

    @BeforeEach
    void setUp(RestDocumentationContextProvider provider) {
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(initController())
                .apply(documentationConfiguration(provider))
                .alwaysDo(print())
                .alwaysDo(restDocs)
                .build();
    }
}