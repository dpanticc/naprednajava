package com.dipl.emailmodule.repository;

import com.dipl.emailmodule.domain.EmailStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface EmailStatusRepository extends JpaRepository<EmailStatusEntity, Long> {
}
