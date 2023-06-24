package ru.practicum.shareit.request;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDtoRequest;
import ru.practicum.shareit.user.dto.UserDtoResponse;
import ru.practicum.shareit.user.dto.UserForItemRequestDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceTest {
    private final ItemRequestService itemRequestService;
    private final UserService userService;
    private final EntityManager em;
    ItemRequest itemRequest1;
    UserDtoRequest userDtoRequest;
    UserDtoRequest userDtoRequest1;
    User owner1;
    User requester;
    LocalDateTime now;
    LocalDateTime nowPlus10min;
    LocalDateTime nowPlus10hours;
    Item item1;
    ItemRequestDto requestDto;
    ItemRequestResponseDto requestResponseDto;
    TypedQuery<ItemRequest> query;

    @BeforeEach
    void setStart() {
        now = LocalDateTime.now();
        nowPlus10min = now.plusMinutes(10);
        nowPlus10hours = now.plusHours(10);

        userDtoRequest = UserDtoRequest.builder()
                .name("name userDto1")
                .email("userDto1@mans.gf")
                .build();
        userDtoRequest1 = UserDtoRequest.builder()
                .name("name userDto2")
                .email("userDto2@mans.gf")
                .build();

        owner1 = User.builder()
                .id(userDtoRequest.getId())
                .name(userDtoRequest.getName())
                .email(userDtoRequest.getEmail())
                .build();

        requester = User.builder()
                .id(userDtoRequest1.getId())
                .name(userDtoRequest1.getName())
                .email(userDtoRequest1.getEmail())
                .build();

        itemRequest1 = ItemRequest.builder()
                .description("description for request 1")
                .requester(requester)
                .created(now)
                .build();

        item1 = Item.builder()
                .name("name for item 1")
                .description("description for item 1")
                .owner(owner1)
                .available(true)
                .build();

        requestDto = ItemRequestDto.builder()
                .description(item1.getDescription())
                .build();

        requestResponseDto = ItemRequestResponseDto.builder()
                .description(item1.getDescription())
                .requester(new UserForItemRequestDto(requester.getId(), requester.getName()))
                .created(now)
                .items(List.of())
                .build();
    }

    @Test
    void addItemRequest() {
        UserDtoResponse savedOwnerDto1 = userService.addUser(userDtoRequest);
        query = em.createQuery("Select ir from ItemRequest ir", ItemRequest.class);
        List<ItemRequest> requestsOfEmpty = query.getResultList();

        assertEquals(0, requestsOfEmpty.size());

        ItemRequestResponseDto saveItemRequest =
                itemRequestService.addItemRequest(savedOwnerDto1.getId(), requestDto);
        List<ItemRequest> afterSave = query.getResultList();

        assertEquals(1, afterSave.size());
        assertEquals(saveItemRequest.getId(), afterSave.get(0).getId());
        assertEquals(saveItemRequest.getCreated(), afterSave.get(0).getCreated());
        assertEquals(saveItemRequest.getDescription(), afterSave.get(0).getDescription());
    }

    @Test
    void addItemRequest_whenRequesterIdIsNull_returnNotFoundRecordInBD() {
        Long requesterId = 9991L;
        assertThrows(ObjectNotFoundException.class,
                () -> itemRequestService.addItemRequest(requesterId, requestDto));
    }

    @Test
    void getItemRequestsByUserId() {
        UserDtoResponse savedUserDto = userService.addUser(userDtoRequest1);
        ItemRequestResponseDto savedItemRequest =
                itemRequestService.addItemRequest(savedUserDto.getId(), requestDto);

        query = em.createQuery("Select ir from ItemRequest ir", ItemRequest.class);

        List<ItemRequestResponseDto> itemsFromDb =
                itemRequestService.getItemRequestsByUserId(savedUserDto.getId());

        assertEquals(1, itemsFromDb.size());

        assertEquals(savedItemRequest.getId(), itemsFromDb.get(0).getId());
        assertEquals(savedItemRequest.getRequester().getId(), itemsFromDb.get(0).getRequester().getId());
        assertEquals(savedItemRequest.getRequester().getName(), itemsFromDb.get(0).getRequester().getName());
        assertEquals(savedItemRequest.getCreated(), itemsFromDb.get(0).getCreated());
        assertEquals(requestResponseDto.getDescription(), itemsFromDb.get(0).getDescription());
    }

    @Test
    void getAllItemRequests() {
        UserDtoResponse saveRequesterDto = userService.addUser(userDtoRequest1);
        UserDtoResponse saveOwnerDto = userService.addUser(userDtoRequest);

        ItemRequestResponseDto savedItemRequest =
                itemRequestService.addItemRequest(saveRequesterDto.getId(), requestDto);

        query = em.createQuery("Select ir from ItemRequest ir where ir.requester.id <> :userId", ItemRequest.class);
        List<ItemRequest> itemRequestList = query.setParameter("userId", saveOwnerDto.getId())
                .getResultList();

        List<ItemRequestResponseDto> emptyItemsFromDbForRequester =
                itemRequestService.getAllItemRequests(saveRequesterDto.getId(), 0, 5);

        assertEquals(0, emptyItemsFromDbForRequester.size());

        List<ItemRequestResponseDto> oneItemFromDbForOwner =
                itemRequestService.getAllItemRequests(saveOwnerDto.getId(), 0, 1);

        assertEquals(savedItemRequest.getId(), oneItemFromDbForOwner.get(0).getId());
        assertEquals(savedItemRequest.getDescription(), oneItemFromDbForOwner.get(0).getDescription());
        assertEquals(savedItemRequest.getRequester().getId(), oneItemFromDbForOwner.get(0).getRequester().getId());
        assertEquals(savedItemRequest.getRequester().getName(), oneItemFromDbForOwner.get(0).getRequester().getName());
        assertEquals(List.of(), oneItemFromDbForOwner.get(0).getItems());
        assertEquals(savedItemRequest.getCreated(), oneItemFromDbForOwner.get(0).getCreated());
    }

    @Test
    void getItemRequest() {
        UserDtoResponse savedRequesterDto = userService.addUser(userDtoRequest1);
        UserDtoResponse observer = userService.addUser(UserDtoRequest.builder().name("nablyudatel").email("1@re.hg").build());

        ItemRequestResponseDto savedItRequest =
                itemRequestService.addItemRequest(savedRequesterDto.getId(), requestDto);

        ItemRequestResponseDto itRequestDtoFromDbObserver =
                itemRequestService.getItemRequest(savedItRequest.getId(), observer.getId());

        assertEquals(savedItRequest.getId(), itRequestDtoFromDbObserver.getId());
        assertEquals(savedItRequest.getCreated(), itRequestDtoFromDbObserver.getCreated());
        assertEquals(savedItRequest.getDescription(), itRequestDtoFromDbObserver.getDescription());
        assertEquals(savedItRequest.getRequester().getId(), itRequestDtoFromDbObserver.getRequester().getId());
        assertEquals(savedItRequest.getRequester().getId(), itRequestDtoFromDbObserver.getRequester().getId());
    }
}