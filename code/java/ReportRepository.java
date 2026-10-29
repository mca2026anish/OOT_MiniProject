package com.cleangoa.cleangoa.repository;

import com.cleangoa.cleangoa.entity.Report;
import com.cleangoa.cleangoa.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByUserOrderByCreatedAtDesc(User user);
    List<Report> findAllByOrderByCreatedAtDesc();
    List<Report> findByStatusOrderByCreatedAtDesc(String status);

    long countByStatus(String status);
}