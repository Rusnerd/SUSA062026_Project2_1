package org.example.bookingexperiences.payment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:booking_test;DB_CLOSE_DELAY=-1;MODE=MySQL",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.generate-ddl=true",
        "spring.jpa.properties.hibernate.hbm2ddl.auto=create-drop",
        "spring.jpa.properties.hibernate.boot.allow_jdbc_metadata_access=true"
})
class PaymentIntentIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void createsThenReadsPaymentIntent() throws Exception {
        UUID id = UUID.randomUUID();
        UUID bookingId = UUID.randomUUID();
        String body = "{\"id\":\"" + id + "\",\"bookingId\":\"" + bookingId + "\",\"amount\":75.50}";

        mockMvc.perform(post("/api/payment-intents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.status").value("CREATED"));

        mockMvc.perform(get("/api/payment-intents/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.amount").value(75.50));
    }

    @Test
    void rejectsSecondStatusChange() throws Exception {
        UUID id = UUID.randomUUID();
        UUID bookingId = UUID.randomUUID();
        String body = "{\"id\":\"" + id + "\",\"bookingId\":\"" + bookingId + "\",\"amount\":75.50}";

        mockMvc.perform(post("/api/payment-intents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/payment-intents/{id}/paid", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"));

        mockMvc.perform(post("/api/payment-intents/{id}/failed", id))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }
}
