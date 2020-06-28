package com.studilieferservice.ProductManager;

import com.studilieferservice.ProductManager.controller.ProductWebController;
import com.studilieferservice.ProductManager.product.Product;
import com.studilieferservice.ProductManager.product.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.mockito.Mockito;
import java.util.Arrays;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProductWebControllerTest {
    @InjectMocks
    private ProductWebController productWebController;

    @Mock
    private ProductService productService;

    private MockMvc mockMvc;

    List<Product> productsTest = Arrays.asList(new Product[]{
                    new Product("webTest", "3.00", 3.00, "https://spring.io/images/spring-logo-9146a4d3298760c2e7e49595184e1975.svg"),
                    new Product("webTest1", "3.00", 3.00, "https://spring.io/images/spring-logo-9146a4d3298760c2e7e49595184e1975.svg"), });

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productWebController).build();
    }

    @Test
    public void listProductsTest() throws Exception{
        String name = "";

        Mockito.when(productService.listAllProducts()).thenReturn(productsTest);
        Mockito.when(productService.findAllWithName(name)).thenReturn(productsTest);

        this.mockMvc.perform(get("/products")
        .param("name",name))
        .andExpect(status().isOk())
        .andExpect(model().attribute("products",productsTest))
        .andExpect(view().name("index"));

    }

}
