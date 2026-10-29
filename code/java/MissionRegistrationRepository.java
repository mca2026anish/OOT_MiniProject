package com.cleangoa.cleangoa.repository;

import com.cleangoa.cleangoa.entity.Mission;
import com.cleangoa.cleangoa.entity.MissionRegistration;
import com.cleangoa.cleangoa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MissionRegistrationRepository
        extends JpaRepository<MissionRegistration, Long> {

    boolean existsByUserAndMission(User user, Mission mission);


    List<MissionRegistration> findByUser(User user);

    long countByMission(Mission mission);

    void deleteByUserAndMission(User user, Mission mission);

    void deleteByMission(Mission mission);
}
