package ru.practicum.shareit.validation;

import org.junit.jupiter.api.BeforeEach;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDtoRequest;
import ru.practicum.shareit.user.model.User;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class ValidationServiceTest {
    private Validator validator;
    Item item;
    User owner1;
    UserDtoRequest ownerDto1;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        ownerDto1 = UserDtoRequest.builder()
                .name("name userDto1")
                .email("userDto1@mans.gf")
                .build();

        owner1 = User.builder()
                .id(ownerDto1.getId())
                .name(ownerDto1.getName())
                .email(ownerDto1.getEmail())
                .build();

        item = Item.builder()
                .name("name for item 1")
                .description("description for item 1")
                .owner(owner1)
                .available(true)
                .build();
    }
}
