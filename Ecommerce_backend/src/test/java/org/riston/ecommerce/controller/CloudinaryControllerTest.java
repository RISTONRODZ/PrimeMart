package org.riston.ecommerce.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.riston.ecommerce.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CloudinaryController.class)
class CloudinaryControllerTest {

    private static final String BASE = "/api/v1/upload/image";
    private static final String JWT = "Bearer mock.jwt.token";
    private static final String CLOUDINARY_URL = "https://res.cloudinary.com/demo/image/upload/v1/primemart/reviews/abc123.jpg";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CloudinaryService cloudinaryService;

    private static MockMultipartFile sampleImage() {
        return new MockMultipartFile(
                "file",
                "test-image.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "fake-image-bytes".getBytes()
        );
    }

    @Nested
    @DisplayName("POST /upload/image")
    class UploadImageTests {

        @Test
        @DisplayName("returns 200 with a Cloudinary URL on successful upload")
        void uploadImage_success() throws Exception {
            when(cloudinaryService.uploadImage(any(), eq("products"))).thenReturn(CLOUDINARY_URL);

            mockMvc.perform(multipart(BASE)
                            .file(sampleImage())
                            .param("folder", "products")
                            .header("Authorization", JWT))
                    .andExpect(status().isOk())
                    .andExpect(content().string(CLOUDINARY_URL));

            verify(cloudinaryService).uploadImage(any(), eq("products"));
        }

        @Test
        @DisplayName("returns 500 when Cloudinary upload fails")
        void uploadImage_cloudinaryFailure_returns500() throws Exception {
            when(cloudinaryService.uploadImage(any(), anyString()))
                    .thenThrow(new IOException("Cloudinary upload failed"));

            mockMvc.perform(multipart(BASE)
                            .file(sampleImage())
                            .param("folder", "products")
                            .header("Authorization", JWT))
                    .andExpect(status().isInternalServerError());
        }
    }
}