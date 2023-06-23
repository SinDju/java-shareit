package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    UserRepository userRepository;
    User user;
    Item item;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("userName1")
                .email("test@mail.fg")
                .build();
        userRepository.save(user);
        itemRepository.save(Item.builder()
                .name("item1")
                .description("item 1 Oh")
                .available(true)
                .request(null)
                .owner(user)
                .build());
        itemRepository.save(Item.builder()
                .name("Boook")
                .description("Soha")
                .available(true)
                .request(null)
                .owner(user)
                .build());
    }

    @Test
    void testFindAllByOwnerOrderById() {
        List<Item> itemList = itemRepository
                .findAllByOwnerIdOrderById(user.getId(), PageRequest.of(0, 2)).getContent();

        assertNotNull(itemList);
        assertEquals(2, itemList.size());
    }

    @Test
    void testSearchItemsByText() {
        Pageable pageable = PageRequest.of(0, 10, Sort.unsorted());
        List<Item> itemList =
                itemRepository.findByNameOrDescription("oh", pageable).getContent();

        assertNotNull(itemList);
        assertEquals(2, itemList.size());
    }
}
