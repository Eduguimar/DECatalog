package com.devedu.decatalog.repositories;

import com.devedu.decatalog.entities.Product;
import com.devedu.decatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository repository;

    private long existingId;
    private long nonExistingId;
    private long countTotalProducts;
    private Product product;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        nonExistingId = 300L;
        countTotalProducts = 25L;
        product = Factory.createProduct();
    }

    @Test
    public void findByIdShouldReturnNonEmptyOptionalProductWhenIdExists() {
        Optional<Product> obj = repository.findById(existingId);

        Assertions.assertFalse(obj.isEmpty());
    }

    @Test
    public void findByIdShouldReturnEmptyOptionalProductWhenIdDoesNotExist() {
        Optional<Product> obj = repository.findById(nonExistingId);

        Assertions.assertTrue(obj.isEmpty());
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
        product.setId(null);
        product = repository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1, product.getId());
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        repository.deleteById(existingId);
        Optional<Product> result = repository.findById(existingId);

        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            repository.deleteById(nonExistingId);
        });
    }
}
