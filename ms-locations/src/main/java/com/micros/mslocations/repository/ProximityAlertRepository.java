package com.micros.mslocations.repository;

import org.springframework.data.repository.CrudRepository;
import com.micros.mslocations.model.ProximityAlert;
import java.util.List;

public interface ProximityAlertRepository extends CrudRepository<ProximityAlert, Long> {
    List<ProximityAlert> findByUserId1(Long userId1);
    List<ProximityAlert> findByUserId2(Long userId2);
}
