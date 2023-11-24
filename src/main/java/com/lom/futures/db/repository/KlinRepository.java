package com.lom.futures.db.repository;

import com.lom.futures.db.entity.Klin;
import com.lom.futures.db.entity.KlinPK;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KlinRepository extends JpaRepository<Klin, KlinPK> {

    List<Klin> findAllByOpenTimeBetween(Long OpenTimeAfter, Long penTimeBefore);
}
