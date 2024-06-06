package com.github.supercoding.repository.storeSales;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreSalesJpaRepository extends JpaRepository<StoreSales,Integer> {
    @Query("SELECT s from StoreSales s join fetch s.itemEntities")
    List<StoreSales> findAllFetchJoin();
}
