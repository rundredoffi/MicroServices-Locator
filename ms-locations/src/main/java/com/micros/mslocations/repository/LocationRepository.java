package com.micros.mslocations.repository;

import org.springframework.data.repository.CrudRepository;
import com.micros.mslocations.model.Location;
import java.util.List;

public interface LocationRepository extends CrudRepository<Location, Long> {
    List<Location> findByUserId(Long userId);
}
