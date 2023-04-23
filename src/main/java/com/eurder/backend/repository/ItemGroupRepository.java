package com.eurder.backend.repository;

import com.eurder.backend.domain.ItemGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ItemGroupRepository extends JpaRepository<ItemGroup, Long> {
    List<ItemGroup> findAllByShippingDateIs(LocalDate localDate);
}
