package com.devedu.decatalog.resources;

import com.devedu.decatalog.dto.ProductDTO;
import com.devedu.decatalog.entities.Category;
import com.devedu.decatalog.services.ProductService;
import com.devedu.decatalog.services.exceptions.DatabaseException;
import com.devedu.decatalog.services.exceptions.ResourceNotFoundException;
import com.devedu.decatalog.tests.Factory;
import com.devedu.decatalog.tests.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductResourceTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TokenUtil tokenUtil;

    @MockBean
    private ProductService service;

    @Autowired
    private ObjectMapper objectMapper;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private ProductDTO productDTO;
    private Category category;
    private PageImpl<ProductDTO> page;

    private String username;
    private String password;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        productDTO = Factory.createProductDTO();
        category = Factory.createCategory();
        page = new PageImpl<>(List.of(productDTO));
        username = "maria@gmail.com";
        password = "123456";

        Mockito.when(service.findAllPaged(any(), any(), any())).thenReturn(page);
        Mockito.when(service.findById(existingId)).thenReturn(productDTO);
        Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
        Mockito.when(service.insert(any())).thenReturn(productDTO);
        Mockito.when(service.update(eq(existingId), any())).thenReturn(productDTO);
        Mockito.when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);
        Mockito.doNothing().when(service).delete(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
        Mockito.doThrow(DatabaseException.class).when(service).delete(dependentId);
    }

    @Test
    public void findAllShouldReturnPage() throws Exception{
        mockMvc.perform(get("/products")
            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenIdExists() throws Exception{
        mockMvc.perform(get("/products/{id}", existingId)
            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.price").exists());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
        mockMvc.perform(get("/products/{id}", nonExistingId)
            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void insertShouldReturnProductDTOCreated() throws Exception{
        String jsonBody = objectMapper.writeValueAsString(productDTO);
        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        mockMvc.perform(post("/products")
            .header("Authorization", "Bearer " + accessToken)
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.price").exists());
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() throws Exception{
        String jsonBody = objectMapper.writeValueAsString(productDTO);
        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        mockMvc.perform(put("/products/{id}", existingId)
            .header("Authorization", "Bearer " + accessToken)
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.price").exists());
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
        String jsonBody = objectMapper.writeValueAsString(productDTO);
        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        mockMvc.perform(put("/products/{id}", nonExistingId)
            .header("Authorization", "Bearer " + accessToken)
            .content(jsonBody)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception{
        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        mockMvc.perform(delete("/products/{id}", existingId)
            .header("Authorization", "Bearer " + accessToken)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        mockMvc.perform(delete("/products/{id}", nonExistingId)
            .header("Authorization", "Bearer " + accessToken)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnBadRequestWhenIntegrityViolation() throws Exception{
        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        mockMvc.perform(delete("/products/{id}", dependentId)
            .header("Authorization", "Bearer " + accessToken)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }
}
