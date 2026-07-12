package com.flow.payflow.repository;

import com.flow.payflow.entity.Store;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByEmail(String email);

    Optional<Store> findByToken(String token);

    @Query(value = "SELECT fc.minimal_amount FROM store s \n" +
           "INNER JOIN fees_config fc ON fc.store_id = s.id \n" +
           "WHERE s.token = :token", nativeQuery = true)
    Long findByMinimalAmountStoreToken(@Param("token") String token);
}
