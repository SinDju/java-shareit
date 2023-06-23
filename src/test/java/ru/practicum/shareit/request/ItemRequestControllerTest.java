package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserForItemRequestDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ItemRequestService itemRequestService;
    @Autowired
    private MockMvc mockMvc;
    private ItemRequestDto itemRequestDto = ItemRequestDto.builder()
            .description("allneeded")
            .build();
    private User user = new User(1L, "ash@gmail.com", "Ash");
    private ItemRequestResponseDto itemRequestResponseDto = new ItemRequestResponseDto(1L,
            "allneeded", new UserForItemRequestDto(user.getId(), user.getName()), LocalDateTime.now()
            , List.of());

    @Test
    void create() throws Exception {
        when(itemRequestService.addItemRequest(anyLong(), any(ItemRequestDto.class)))
                .thenReturn(itemRequestResponseDto);

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemRequestResponseDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestResponseDto.getDescription()), String.class))
                .andExpect(jsonPath("$.requester.id", is(itemRequestResponseDto.getRequester().getId()), Long.class));
    }

    @Test
    void getItemRequestsByUserId() throws Exception {
        when(itemRequestService.getItemRequestsByUserId(anyLong()))
                .thenReturn(List.of(itemRequestResponseDto));
        mockMvc.perform(get("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void getItemRequest() throws Exception {
        when(itemRequestService.getItemRequest(anyLong(), anyLong()))
                .thenReturn(itemRequestResponseDto);
        mockMvc.perform(get("/requests/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void getAllRequests() throws Exception {
        when(itemRequestService.getAllItemRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemRequestResponseDto));
        mockMvc.perform(get("/requests/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk());
    }
}
