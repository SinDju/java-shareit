package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.mapper.ItemRequestDtoMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.dto.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ItemRequestServiceUnitTest {
    private final ItemRequestRepository mockItemRequestRepository = Mockito.mock(ItemRequestRepository.class);
    private final UserRepository mockUserRepository = Mockito.mock(UserRepository.class);
    private final ItemRepository mockItemRepository = Mockito.mock(ItemRepository.class);
    private final ItemRequestService mockItemRequestService = Mockito.mock(ItemRequestService.class);
    private final UserService mockUserService = Mockito.mock(UserService.class);

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    private User user;
    private UserService userService;
    private ItemRequestDto itemRequestDto;
    private ItemRequest itemRequest;
    private Item item;


    @BeforeEach
    void toStart() {
        user = User.builder()
                .id(1L)
                .name("userName1")
                .email("test@mail.fg")
                .build();
        itemRequestDto = ItemRequestDto.builder()
                .description("description itemRequest")
                .build();
        itemRequest = ItemRequestDtoMapper.toItemRequest(itemRequestDto, user);
        item = Item.builder()
                .id(1L)
                .name("Item")
                .description("description item")
                .available(true)
                .request(null)
                .owner(user)
                .build();
    }


    @Test
    void getRequestsInformationEmpty() {
        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        List<ItemRequestResponseDto> requestDtoList = itemRequestService.getItemRequestsByUserId(user.getId());
        assertEquals(0, requestDtoList.size());
    }


    @Test
    void getRequestsInformationWrongUser() {
        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        ObjectNotFoundException ex = assertThrows(ObjectNotFoundException.class, () -> itemRequestService.getItemRequestsByUserId(1L));
        assertEquals("Пользователь с ID 1 не зарегистрирован!", ex.getMessage());
    }

    @Test
    void getRequestInformationWrongRequest() {
        when(mockUserRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(user));
        when(mockItemRequestRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        ObjectNotFoundException ex =  assertThrows(ObjectNotFoundException.class, () -> itemRequestService.getItemRequest(1L, user.getId()));
        assertEquals("Запрос c ID 1 не найден", ex.getMessage());
    }

    @Test
    public void getAllRequest_validInput_returnsList() {
        Long userId = 1L;
        Integer from = 0;
        Integer size = 10;
        User user = new User();
        user.setId(userId);
        when(mockUserRepository.existsById(userId)).thenReturn(true);

        List<ItemRequest> itemRequests = new ArrayList<>();
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);
        User requestor = new User();
        requestor.setId(2L);
        itemRequest.setRequester(requestor);
        itemRequests.add(itemRequest);
        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        Pageable page = PageRequest.of(from / size, size, sort);
        when(mockItemRequestRepository.findAllByNotRequesterId(userId,
                page))
                .thenReturn(new PageImpl<>(itemRequests));
        when(mockUserRepository.findById(requestor.getId())).thenReturn(Optional.of(requestor));
        List<ItemRequestResponseDto> expectedItemRequestDtoList = new ArrayList<>();
        ItemRequestResponseDto itemRequestDto = new ItemRequestResponseDto();
        itemRequestDto.setId(1L);
        UserDtoRequest requestorDto = new UserDtoRequest(
                2L,
                null,
                null
        );
        itemRequestDto.setRequester(new UserForItemRequestDto(requestorDto.getId(), requestorDto.getName()));
        itemRequestDto.setItems(new ArrayList<>());
        expectedItemRequestDtoList.add(itemRequestDto);
        List<ItemRequestResponseDto> actualItemRequestDtoList = itemRequestService.getAllItemRequests(userId, from, size);

        assertEquals(expectedItemRequestDtoList, actualItemRequestDtoList);
    }
}
