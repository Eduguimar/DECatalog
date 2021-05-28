package com.devedu.decatalog.resources;

import com.devedu.decatalog.dto.ProductDTO;
import com.devedu.decatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private long existingId;
    private long nonExistingId;
    private long countTotalProducts;
    ProductDTO productDTO;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 300L;
        countTotalProducts = 25L;
        productDTO = Factory.createProductDTO();
    }

    @Test
    public void findAllShouldReturnSortedPageWhenSortByName() throws Exception {
        ResultActions resultFirstPage = mockMvc.perform(get("/products?page=0&size=12&sort=name,asc")
                .accept(MediaType.APPLICATION_JSON));

        ResultActions resultLastPage = mockMvc.perform(get("/products?page=2&size=12&sort=name,asc")
                .accept(MediaType.APPLICATION_JSON));

        resultFirstPage.andExpect(status().isOk());
        resultFirstPage.andExpect(jsonPath("$.totalElements").value(countTotalProducts));
        resultFirstPage.andExpect(jsonPath("$.content").exists());
        resultFirstPage.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
        resultFirstPage.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
        resultFirstPage.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));
        resultFirstPage.andExpect(jsonPath("$.content[11].name").value("PC Gamer Max"));

        resultLastPage.andExpect(jsonPath("$.content[0].name").value("The Lord of the Rings"));
        resultLastPage.andExpect(jsonPath("$.content[0].date").value("2020-07-13T20:50:07.123450Z"));
        resultLastPage.andExpect(jsonPath("$.pageable.pageNumber").value(2));
        resultLastPage.andExpect(jsonPath("$.pageable.pageSize").value(12));
    }

    @Test
    public void updateShouldReturnProductDTOWhenIdExists() throws Exception{
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        String expectedName = productDTO.getName();
        String expectedDescription = productDTO.getDescription();
        Double expectedPrice = productDTO.getPrice();

        ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.name").value(expectedName))
                .andExpect(jsonPath("$.description").value(expectedDescription))
                .andExpect(jsonPath("$.price").value(expectedPrice));
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }
}
