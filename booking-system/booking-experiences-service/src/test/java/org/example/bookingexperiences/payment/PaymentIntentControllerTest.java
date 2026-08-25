package org.example.bookingexperiences.payment;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentIntentController.class)
@Import(RestExceptionHandler.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class PaymentIntentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentIntentService paymentIntentService;

    @Test
    void createsPaymentIntentThroughApi() throws Exception {
        UUID id = UUID.randomUUID();
        UUID bookingId = UUID.randomUUID();
        PaymentIntent intent = new PaymentIntent(id, bookingId, new BigDecimal("25.00"), PaymentStatus.CREATED);
        when(paymentIntentService.createPaymentIntent(id, bookingId, new BigDecimal("25.00"))).thenReturn(intent);

        String body = "{\"id\":\"" + id + "\",\"bookingId\":\"" + bookingId + "\",\"amount\":25.00}";

        mockMvc.perform(post("/api/payment-intents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    void rejectsInvalidAmount() throws Exception {
        String body = "{\"id\":\"" + UUID.randomUUID() + "\",\"bookingId\":\"" + UUID.randomUUID() + "\",\"amount\":0}";

        mockMvc.perform(post("/api/payment-intents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void returnsPaymentIntentById() throws Exception {
        UUID id = UUID.randomUUID();
        PaymentIntent intent = new PaymentIntent(id, UUID.randomUUID(), new BigDecimal("15.00"), PaymentStatus.CREATED);
        when(paymentIntentService.getById(id)).thenReturn(intent);

        mockMvc.perform(get("/api/payment-intents/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    void returnsNotFoundForMissingPaymentIntent() throws Exception {
        UUID id = UUID.randomUUID();
        when(paymentIntentService.getById(id))
                .thenThrow(new PaymentIntentNotFoundException("Payment intent not found: " + id));

        mockMvc.perform(get("/api/payment-intents/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void returnsBadRequestForMalformedUuid() throws Exception {
        mockMvc.perform(get("/api/payment-intents/not-a-uuid"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void returnsConflictForInvalidTransition() throws Exception {
        UUID id = UUID.randomUUID();
        when(paymentIntentService.markPaid(id))
                .thenThrow(new InvalidPaymentStatusTransitionException(PaymentStatus.PAID, PaymentStatus.PAID));

        mockMvc.perform(post("/api/payment-intents/{id}/paid", id))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }
}
