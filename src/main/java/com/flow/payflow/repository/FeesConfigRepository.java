package com.flow.payflow.repository;

import com.flow.payflow.entity.FeesConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeesConfigRepository extends JpaRepository<FeesConfig, Long> {

    Optional<FeesConfig> findByStoreId(Long storeId);
}
