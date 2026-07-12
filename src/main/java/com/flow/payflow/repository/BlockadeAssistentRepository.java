package com.flow.payflow.repository;

import com.flow.payflow.entity.BlockadeAssistent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlockadeAssistentRepository extends JpaRepository<BlockadeAssistent, Long> {

    Optional<BlockadeAssistent> findByCard(String card);
}
