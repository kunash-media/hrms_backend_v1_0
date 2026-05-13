package com.hrms.repository;

import com.hrms.entity.OtpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpEntity, Long> {


    @Query("SELECT o FROM OtpEntity o WHERE o.email = :email " +
            "AND o.expiresAt > :now AND o.isUsed = false ORDER BY o.createdAt DESC")
    List<OtpEntity> findValidEmailOtps(@Param("email") String email,
                                       @Param("now")   LocalDateTime now);


    @Query("SELECT o FROM OtpEntity o WHERE o.email = :email ORDER BY o.createdAt DESC LIMIT 1")
    Optional<OtpEntity> findLatestByEmail(@Param("email") String email);


    @Modifying
    @Query("DELETE FROM OtpEntity o WHERE o.email = :email")
    void deleteByEmail(@Param("email") String email);


    @Modifying
    @Query("DELETE FROM OtpEntity o WHERE o.expiresAt < :currentTime")
    void deleteExpiredOtps(@Param("currentTime") LocalDateTime currentTime);
}
