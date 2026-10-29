package com.cleangoa.cleangoa.repository;

import com.cleangoa.cleangoa.entity.Mission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissionRepository extends JpaRepository<Mission, Long> {

    List<Mission> findAllByOrderByEventDateAsc();
}
