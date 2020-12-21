package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Consumer;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Consumer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConsumerRepository extends JpaRepository<Consumer, Long> {
}
