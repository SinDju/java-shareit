package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDtoRequest;
import ru.practicum.shareit.user.dto.UserForItemRequestDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CommentMapperTest {
    ItemRequest itemRequest1;
    ItemRequest itemRequest2;
    UserDtoRequest ownerDto1;
    User user;
    User owner;
    User owner1;
    UserForItemRequestDto requesterDto101;
    User requester101;
    UserDtoRequest bookerDto;
    User booker;
    UserDtoRequest userDtoForTest;
    User userForTest;
    LocalDateTime now;
    LocalDateTime nowPlus10min;
    LocalDateTime nowPlus10hours;
    Item item1;
    ItemRequestDto itemDto1;
    ItemRequestDto itemRequestDto1;
    ItemSearchOfTextDto itemSearchOfTextDto;
    ItemForItemRequestResponseDto itemForItemRequestResponseDto;
    ItemWithBookingDto itemWithBookingDto;
    Booking booking1;
    BookingDtoRequest bookingDto1;
    CommentDtoRequest commentDto;
    ItemDtoRequest itemDtoRequest1;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();
        UserDtoRequest userDto = new UserDtoRequest(1L,
                "name",
                "mail@gmail.com"
        );

        user = User.builder()
                .id(1L)
                .name("name")
                .email("mail@gmail.com")
                .build();


        UserDtoRequest userDto2 = new UserDtoRequest(2L,
                "name owner 2",
                "owner@jjgv.zw"
        );

        owner = User.builder()
                .id(2L)
                .name("name owner 2")
                .email("owner@jjgv.zw")
                .build();

        requesterDto101 = UserForItemRequestDto.builder()
                .name("name requesterDto101")
                .build();

        requester101 = User.builder()
                .id(requesterDto101.getId())
                .name(requesterDto101.getName())
                .email("requesterDto101@mans.gf")
                .build();

        userDtoForTest = UserDtoRequest.builder()
                .name("name userDtoForTest")
                .email("userDtoForTest@userDtoForTest.zx")
                .build();

        userForTest = User.builder()
                .name(userDtoForTest.getName())
                .email(userDtoForTest.getEmail())
                .build();

        bookerDto = UserDtoRequest.builder()
                .name("booker")
                .email("booker@wa.dzd")
                .build();

        booker = User.builder()
                .name(bookerDto.getName())
                .email(bookerDto.getEmail())
                .build();

        itemRequest1 = ItemRequest.builder()
                .description("description for request 1")
                .requester(requester101)
                .created(now)
                .build();
        itemDtoRequest1 = ItemDtoRequest.builder()
                .id(1L)
                .name("name for item 1")
                .description("description for item 1")
                .available(true)
                .build();

        item1 = Item.builder()
                .id(1L)
                .name("name for item 1")
                .description("description for item 1")
                .owner(owner1)
                .available(true)
                .request(itemRequest1)
                .build();

        itemDto1 = ItemRequestDto.builder()
                .description(item1.getDescription())
                .build();

        itemRequestDto1 = ItemRequestDto.builder()
                .description(item1.getDescription())
                .build();

        ItemDtoRequest itemDtoRequest = ItemDtoRequest.builder()
                .id(2L)
                .name("book")
                .description("read book")
                .available(true)
                .build();

        Item item = ItemMapper.toItem(itemDtoRequest);

        commentDto = CommentDtoRequest.builder()
                .text("comment 1")
                .build();

        itemForItemRequestResponseDto = ItemForItemRequestResponseDto.builder()
                .id(item1.getId())
                .available(true)
                .requestId(itemRequest1.getId())
                .name(item1.getName())
                .description(item1.getDescription())
                .build();

        itemWithBookingDto = ItemWithBookingDto.builder()
                .id(item1.getId())
                .name(item1.getName())
                .build();

        itemSearchOfTextDto = ItemMapper.toItemSearchOfTextDto(item);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void toCommentDtoResponseTest() {
        var original = new Comment();
        original.setId(1L);
        original.setText("Cool");
        original.setAuthor(user);
        original.setCreated(LocalDateTime.now());
        var result = CommentMapper.toCommentDtoResponse(original);

        assertNotNull(result);
        assertEquals(original.getId(), result.getId());
        assertEquals(original.getText(), result.getText());
        assertEquals(original.getAuthor().getName(), result.getAuthorName());
        assertEquals(original.getCreated(), result.getCreated());
    }

    @Test
    void toNewCommentTest() {
        var original = new CommentDtoRequest();
        original.setText("Cool");
        var result = CommentMapper.toComment(original, item1, user);

        assertNotNull(result);
        assertEquals(original.getText(), result.getText());
        assertEquals(user, result.getAuthor());
        assertEquals(item1, result.getItem());
        assertNotNull(result.getCreated());
    }

    @Test
    void toCommentDtoListTest() {
        var original = new Comment();
        original.setId(1L);
        original.setText("Cool");
        original.setAuthor(user);
        original.setCreated(LocalDateTime.now());
        var comments = new ArrayList<Comment>();
        comments.add(original);
        var result = CommentMapper.toCommentDtoList(comments);

        assertNotNull(result);
        assertEquals(comments.get(0).getText(), result.get(0).getText());
        assertEquals(comments.get(0).getAuthor().getName(), result.get(0).getAuthorName());
        assertEquals(comments.get(0).getCreated(), result.get(0).getCreated());
    }
}
