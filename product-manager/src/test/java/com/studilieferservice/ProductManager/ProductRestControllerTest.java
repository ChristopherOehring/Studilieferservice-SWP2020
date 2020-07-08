package com.studilieferservice.ProductManager;

import com.studilieferservice.ProductManager.controller.ProductRestController;
import com.studilieferservice.ProductManager.product.Product;
import com.studilieferservice.ProductManager.product.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Arrays;
import java.util.List;

public class ProductRestControllerTest {

    @InjectMocks
    private ProductRestController productRestController;

    @Mock
    private ProductService productService;

    Product product = new Product("Milch", "3.00", 3.00,
            "https://spring.io/images/spring-logo-9146a4d3298760c2e7e49595184e1975.svg");

    List<Product> products = Arrays.asList(product,
            new Product("webTest1", "3.00", 3.00,
                    "https://spring.io/images/spring-logo-9146a4d3298760c2e7e49595184e1975.svg"));

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createProductPerRestTest(){

        Mockito.when(productService.createProduct(product)).thenReturn(product);

        ResponseEntity<?> responseEntity = productRestController.createProduct(product);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody())
                .isEqualTo(product);
    }

    @Test
    public void deleteProductTest(){

        productRestController.deleteProduct(product);
        verify(productService).deleteProductById(product.getName());
    }

    @Test
    public void searchProductTest(){
        String productName = "Milch";

        Mockito.when( productService.findProductById(productName)).thenReturn(product);
        Product response = productRestController.searchProduct(productName);

        assertThat(response).isEqualTo(product);
        assertThat(response.getName()).isEqualTo(productName);
    }

    @Test
    public void listAllProductsTest(){
        Mockito.when( productService.listAllProducts()).thenReturn(products);
        List<Product> response = productRestController.listAllProducts();

        assertThat(response).isEqualTo(products);
        assertThat(response.size()).isEqualTo(2);
        assertThat(response.get(0).getName()).isEqualTo(product.getName());
    }
}
