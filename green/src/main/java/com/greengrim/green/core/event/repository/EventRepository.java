package com.greengrim.green.core.event.repository;

import java.util.Optional;

import com.greengrim.green.core.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findFirstEventByOrderByCreatedAtDesc();
}
