package com.studilieferservice.ProductManager;

import com.studilieferservice.ProductManager.product.Product;
import com.studilieferservice.ProductManager.product.ProductRepository;
import com.studilieferservice.ProductManager.product.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ProductService.class)
public class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    Product product = new Product("webTest", "3.00", 3.00, "https://spring.io/images/spring-logo-9146a4d3298760c2e7e49595184e1975.svg");
    List<Product> productsTest = Arrays.asList(new Product[]{
            new Product("webTest", "3.00", 3.00, "https://spring.io/images/spring-logo-9146a4d3298760c2e7e49595184e1975.svg"),
            new Product("webTest1", "3.00", 3.00, "https://spring.io/images/spring-logo-9146a4d3298760c2e7e49595184e1975.svg"), });

    @Test
    public void createProductTest(){
        Mockito.when(productRepository.save(product)).thenReturn(product);
        assertThat(productService.createProduct(product)).isEqualTo(product);
    }

    @Test
    public void deleteProductByIdTest(){
        productService.deleteProductById(product.getName());
        verify(productRepository).deleteById(product.getName());
    }

    @Test
    public void findProductByIdTest(){
        Mockito.when(productRepository.findById(product.getName()))
                .thenReturn(java.util.Optional.ofNullable(product));
        assertThat(productService.findProductById(product.getName()))
                .isEqualTo(product);

    }

    @Test
    public void findAllWithNameTest(){
        Mockito.when(productRepository.findByNameLike("%"+product.getName()+"%"))
                .thenReturn(productsTest);
        assertThat(productService.findAllWithName(product.getName()))
                .isEqualTo(productsTest);

    }
    @Test
    public void listAllProductsTest(){
        Mockito.when(productRepository.findAll())
                .thenReturn(productsTest);
        assertThat(productService.listAllProducts())
                .isEqualTo(productsTest);

    }

}
