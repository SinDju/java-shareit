package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemWithBookingDto;
import ru.practicum.shareit.user.dto.UserWithIdDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
public class BookingDtoJsonTest {
    @Autowired
    private JacksonTester<BookingForResponse> json;
    @Autowired
    private JacksonTester<BookingDtoRequest> dtoRequestJacksonTester;

    @Test
    void testBookingForResponse() throws IOException {
        LocalDateTime time = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        ItemWithBookingDto itemWithBookingDto = ItemWithBookingDto.builder()
                .id(1L)
                .name("Sukiyaki")
                .build();


        UserWithIdDto userWithIdDto = UserWithIdDto.builder()
                .id(1L)
                .build();

        BookingForResponse bookingDto = BookingForResponse.builder()
                .id(1L)
                .start(time.plusMinutes(1))
                .end(time.plusMinutes(2))
                .item(itemWithBookingDto)
                .booker(userWithIdDto)
                .status(Status.WAITING)
                .build();
        JsonContent<BookingForResponse> result = json.write(bookingDto);

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo(time.plusMinutes(1)
                        .truncatedTo(ChronoUnit.SECONDS)
                        .toString());
        assertThat(result).extractingJsonPathStringValue("$.end")
                .isEqualTo(time.plusMinutes(2)
                        .truncatedTo(ChronoUnit.SECONDS)
                        .toString());
        assertThat(result).extractingJsonPathNumberValue("$.item.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.name")
                .isEqualTo("Sukiyaki");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id")
                .isEqualTo(1);
    }

    @Test
    void testBookingDtoRequest() throws IOException {
        LocalDateTime dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        ItemWithBookingDto itemWithBookingDto = ItemWithBookingDto.builder()
                .id(1L)
                .name("Sukiyaki")
                .build();

        BookingDtoRequest bookingDtoRequest = BookingDtoRequest.builder()
                .start(dateTime.plusMinutes(1))
                .end(dateTime.plusMinutes(2))
                .itemId(itemWithBookingDto.getId())
                .build();
        JsonContent<BookingDtoRequest> result = dtoRequestJacksonTester.write(bookingDtoRequest);


        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo(dateTime.plusMinutes(1)
                        .truncatedTo(ChronoUnit.SECONDS)
                        .toString());
        assertThat(result).extractingJsonPathStringValue("$.end")
                .isEqualTo(dateTime.plusMinutes(2)
                        .truncatedTo(ChronoUnit.SECONDS)
                        .toString());
        assertThat(result).extractingJsonPathNumberValue("$.itemId")
                .isEqualTo(1);
    }
}
