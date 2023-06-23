package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingForResponse;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.ObjectBadRequestException;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.item.model.Item;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private BookingService bookingService;
    @Autowired
    private MockMvc mvc;
    private static final String BASE_PATH_BOOKINGS = "/bookings";
    private final Item item = Item.builder()
            .id(1L)
            .name("testItem")
            .description("testDescription")
            .available(true)
            .build();
    private final BookingDtoRequest inputBookingDto = BookingDtoRequest.builder()
            .start(LocalDateTime.of(2222, 12, 12, 12, 12, 12))
            .end(LocalDateTime.of(2223, 12, 12, 12, 12, 12))
            .itemId(1L)
            .build();
    private final BookingDtoRequest invalidInputBookingDtoWithWrongStart = BookingDtoRequest.builder()
            .start(LocalDateTime.of(1111, 12, 12, 12, 12, 12))
            .end(LocalDateTime.of(2223, 12, 12, 12, 12, 12))
            .itemId(1L)
            .build();
    private final BookingForResponse bookingDto = BookingForResponse.builder()
            .start(LocalDateTime.of(2222, 12, 12, 12, 12, 12))
            .end(LocalDateTime.of(2223, 12, 12, 12, 12, 12))
            .item(new ItemWithBookingDto(item.getId(), item.getName()))
            .build();

    @Test
    void createValidBooking() throws Exception {
        when(bookingService.addBooking(anyLong(), any()))
                .thenReturn(bookingDto);
        mvc.perform(post(BASE_PATH_BOOKINGS)
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(inputBookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDto)));
    }

    @Test
    void createBookingWithWrongStart_shouldReturnStatus400() throws Exception {
        when(bookingService.addBooking(anyLong(), any()))
                .thenReturn(bookingDto);
        mvc.perform(post(BASE_PATH_BOOKINGS)
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(invalidInputBookingDtoWithWrongStart))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateBooking() throws Exception {
        when(bookingService.updateBooking(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(bookingDto);

        mvc.perform(patch(BASE_PATH_BOOKINGS + "/1?approved=true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDto)));
    }

    @Test
    void getBookingById() throws Exception {
        when(bookingService.getBooking(anyLong(), anyLong()))
                .thenReturn(bookingDto);

        mvc.perform(get(BASE_PATH_BOOKINGS + "/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookingDto)));
    }

    @Test
    void getAllBookingsByUser() throws Exception {
        when(bookingService.getAllBookingByUser(anyString(), anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get(BASE_PATH_BOOKINGS + "?state=ALL")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(bookingDto))));
    }

    @Test
    void getAllUserItemsBookings() throws Exception {
        when(bookingService.getAllBookingByOwner(anyString(), anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));

        mvc.perform(get(BASE_PATH_BOOKINGS + "/owner?state=ALL")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(bookingDto))));
    }

    @Test
    public void shouldFailOnApproveWithErrorParam() throws Exception {
        Mockito.when(
                        bookingService.updateBooking(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyBoolean())
                )
                .thenThrow(new ObjectBadRequestException("BadRequest"));

        mvc.perform(patch("/bookings/{bookingId}", "1")
                        .header("X-Sharer-User-Id", 2L)
                        .param("approved", "true")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> Assertions.assertNotNull(result.getResolvedException()));
    }
}