package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDtoRequest;
import ru.practicum.shareit.item.dto.CommentDtoResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class ItemServiceImplTest {
    LocalDateTime now = LocalDateTime.now();

    @Test
    void getItemWithBookingAndComment() {
        CommentDtoRequest inputCommentDto = CommentDtoRequest.builder()
                .text("new comment for test")
                .build();

        User owner2 = User.builder()
                .id(2L)
                .name("name for owner")
                .email("owner2@aadmf.wreew")
                .build();

        User userForTest2 = User.builder()
                .id(1L)
                .name("name user for test 2")
                .email("userForTest2@ahd.ew")
                .build();

        Item zaglushka = Item.builder()
                .id(1L)
                .name("zaglushka")
                .description("desc item zaglushka")
                .owner(owner2).build();

        Booking bookingFromBd = Booking.builder()
                .id(1L)
                .item(zaglushka)
                .booker(userForTest2)
                .start(now.minusDays(10))
                .end(now.minusDays(5))
                .build();

        Item itemFromBd = Item.builder()
                .id(1L)
                .name("name for item")
                .description("desc for item")
                .owner(owner2)
                .available(true)
                .build();

        CommentDtoResponse commentDto = CommentDtoResponse.builder()
                .id(1L)
                .text("comment 1")
                .authorName("name user for test 2")
                .created(now.minusDays(5))
                .build();

        Comment outputComment = Comment.builder()
                .id(1L)
                .author(userForTest2)
                .text("comment 1")
                .item(itemFromBd)
                .build();

        UserRepository userRepositoryJpa2 = mock(UserRepository.class);
        ItemRepository itemRepositoryJpa2 = mock(ItemRepository.class);
        CommentRepository commentRepository2 = mock(CommentRepository.class);

        when(userRepositoryJpa2.findById(any())).thenReturn(Optional.of(userForTest2));
        when(itemRepositoryJpa2.findById(any())).thenReturn(Optional.of(itemFromBd));
        when(commentRepository2.save(any())).thenReturn(outputComment);
    }

   /* @Test
    public void testToItemBookingInfoDto_positive() {
        Booking booking = Booking.builder()
                .id(1L)
                .booker(User.builder().id(2L).build())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusDays(1))
                .build();

        BookingForItemDto itemBookingInfoDto = BookingMapper.toBookingForItemDto(booking);

        assertEquals(1L, itemBookingInfoDto.getId());
        assertEquals(2L, itemBookingInfoDto.getBookerId());
        assertEquals(booking.getStart(), itemBookingInfoDto.getStart());
        assertEquals(booking.getEnd(), itemBookingInfoDto.getEnd());
    }*/
}
