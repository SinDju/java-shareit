package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findAllByOwnerIdOrderById(Long userId, Pageable pageable);

    @Query(" select i from Item i where i.available = true and " +
            "(upper(i.name) like upper(concat('%', ?1, '%'))" +
            "or upper(i.description) like upper(concat('%', ?1, '%')))")
    Page<Item> findByNameOrDescription(
            String text, Pageable pageable);

    @Query("select i from Item i " +
            "where i.request.id = ?1")
    List<Item> findAllByRequestId(Long requestId);

    List<Item> findByRequestIdIn(List<Long> requestIds);
}
