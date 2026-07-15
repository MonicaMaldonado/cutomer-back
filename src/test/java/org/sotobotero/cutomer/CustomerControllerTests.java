package org.sotobotero.cutomer;

import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.sotobotero.customer.controller.CustomerController;
import org.sotobotero.customer.entities.Customer;
import org.sotobotero.customer.repository.CustomerRepository;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomerControllerTests extends AbstractIntegrationTest {

    private MockMvc mockMvc;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerController customerController;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    public void getAllCustomersTest() throws Exception {
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer(1L, "John Doe", "johndoe@example.com", "123456789",
                "123 Main Street", "Anytown", "Anystate", "US", "12345",
                "ABC Inc.", "CEO", "www.example.com", "twitter_handle", "facebook_url",
                "linkedin_profile", "github_handle", "instagram_handle", "youtube_url",
                "tiktok_handle", "snapchat_handle", "twitch_handle", "other", "notes", 34));

        when(customerRepository.findAll()).thenReturn(customers);

        mockMvc.perform(get("/api/v1/customer")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("John Doe")))
                .andExpect(jsonPath("$[0].email", is("johndoe@example.com")));
    }

    @Test
    public void getCustomerByIdTest() throws Exception {
        Long customerId = 1L;
        String customerIdString = customerId.toString();
        Customer customer = new Customer(customerId, "John Doe", "johndoe@example.com", "123456789",
                "123 Main Street", "Anytown", "Anystate", "US", "12345",
                "ABC Inc.", "CEO", "www.example.com", "twitter_handle", "facebook_url",
                "linkedin_profile", "github_handle", "instagram_handle", "youtube_url",
                "tiktok_handle", "snapchat_handle", "twitch_handle", "other", "notes", 34);

        when(customerRepository.findById(customerIdString)).thenReturn(Optional.of(customer));

        mockMvc.perform(get("/api/v1/customer/{id}", customerId)
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John Doe")));
    }

    @Test
    public void createCustomerTest() throws Exception {
        Customer customer = new Customer(null, "John Doe", "johndoe@example.com", "123456789",
                "123 Main Street", "Anytown", "Anystate", "US", "12345",
                "ABC Inc.", "CEO", "www.example.com", "twitter_handle", "facebook_url",
                "linkedin_profile", "github_handle", "instagram_handle", "youtube_url",
                "tiktok_handle", "snapchat_handle", "twitch_handle", "other", "notes", 60);

        mockMvc.perform(post("/api/v1/customer")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated());
    }

    @Test
    public void givenInvalidCustomerId_UpdateCustomer_NotFound() throws Exception {
        // Corregido: Ajustada la URL para que coincida con la ruta real del controlador
        String url = "/api/v1/customer"; 
        Customer customer = new Customer();
        customer.setId(100L); // Asignamos un ID que no existirá
        customer.setName("Jane Doe");
        customer.setEmail("janedoe@gmail.com");
        
        String requestbody = objectMapper.writeValueAsString(customer);
        
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestbody))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
        
        assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    public void updateCustomerTest() throws Exception {
        Long customerId = 1L;
        String customerIdString = customerId.toString();      
        Customer customer = new Customer(customerId, "Jane Doe", "johndoe@example.com", "123456789",
                "123 Main Street", "Anytown", "Anystate", "US", "12345",
                "ABC Inc.", "CEO", "www.example.com", "twitter_handle", "facebook_url",
                "linkedin_profile", "github_handle", "instagram_handle", "youtube_url",
                "tiktok_handle", "snapchat_handle", "twitch_handle", "other", "notes", 30);
                
        when(customerRepository.findById(customerIdString)).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);

        // ¡CORREGIDO! Cambiado updatecustomer a updateCustomer (C mayúscula)
        Customer body = customerController.updateCustomer(customer).getBody();
        assertEquals(customer.getName(), body.getName());
    }

    @Test
    public void updateCustomerNameTest() throws Exception {
        Long customerId = 1L;
        String customerIdString = customerId.toString();
        String newName = "Jane Doe";
        Customer customer = new Customer(customerId, newName, "johndoe@example.com", "123456789",
                "123 Main Street", "Anytown", "Anystate", "US", "12345",
                "ABC Inc.", "CEO", "www.example.com", "twitter_handle", "facebook_url",
                "linkedin_profile", "github_handle", "instagram_handle", "youtube_url",
                "tiktok_handle", "snapchat_handle", "twitch_handle", "other", "notes", 30);

        when(customerRepository.findById(customerIdString)).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(customer);

        mockMvc.perform(patch("/api/v1/customer/{id}", customerId)
                .contentType("application/json")
                .param("name", newName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(newName)));
    }

    @Test
    public void deleteCustomerTest() throws Exception {
        Long customerId = 1L;
        String customerIdString = customerId.toString();
        Customer customer = new Customer(customerId, "John Doe", "johndoe@example.com", "123456789",
                "123 Main Street", "Anytown", "Anystate", "US", "12345",
                "ABC Inc.", "CEO", "www.example.com", "twitter_handle", "facebook_url",
                "linkedin_profile", "github_handle", "instagram_handle", "youtube_url",
                "tiktok_handle", "snapchat_handle", "twitch_handle", "other", "notes", 18);
        
        when(customerRepository.findById(customerIdString)).thenReturn(Optional.of(customer));

        mockMvc.perform(delete("/api/v1/customer/{id}", customerId)
                .contentType("application/json"))
                .andExpect(status().isOk());
    }
}