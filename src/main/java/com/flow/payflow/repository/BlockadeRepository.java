package com.flow.payflow.repository;

import com.flow.payflow.entity.Blockade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlockadeRepository extends JpaRepository<Blockade, Long> {
    Optional<Blockade> findByStoreId(Long storeId);
}
