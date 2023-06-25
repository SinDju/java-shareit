package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.data.domain.Sort.Direction.DESC;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CommentRepositoryTest {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemRequestRepository itemRequestRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CommentRepository commentRepository;
    User user;
    User user1;
    Item item;
    Item item3;
    Comment comment;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("userName1")
                .email("test@mail.fg")
                .build();
        userRepository.save(user);
        user1 = User.builder()
                .name("userName2")
                .email("test2@mail.fg")
                .build();
        userRepository.save(user1);
        itemRepository.save(Item.builder()
                .name("item1")
                .description("item 1 Oh")
                .available(true)
                .owner(user)
                .build());
        itemRepository.save(Item.builder()
                .name("Boook")
                .description("Soha")
                .available(true)
                .owner(user)
                .build());
        ItemRequest itemRequest = ItemRequest.builder()
                .created(LocalDateTime.now().minusDays(1))
                .requester(user1)
                .description("test cock")
                .build();
        itemRequestRepository.save(itemRequest);
        item3 = Item.builder()
                .name("Cook")
                .description("test cock")
                .available(true)
                .owner(user)
                .request(itemRequest)
                .build();
        itemRepository.save(item3);
        itemRequest.setItems(List.of(item3));
        comment = Comment.builder()
                .text("test cock - real cook")
                .author(itemRequest.getRequester())
                .item(item3)
                .created(LocalDateTime.now())
                .build();
        commentRepository.save(comment);
    }

    @Test
    void testFindAllByOwnerOrderById() {
        List<Comment> commentList = commentRepository.findByItemIn(List.of(item3), Sort.by(DESC, "created"));

        assertNotNull(commentList);
        assertEquals(1, commentList.size());
        assertEquals(item3, commentList.get(0).getItem());
        assertEquals(comment.getAuthor(), commentList.get(0).getAuthor());
        assertEquals(comment.getText(), commentList.get(0).getText());
        assertEquals(comment.getCreated(), commentList.get(0).getCreated());
    }
}
