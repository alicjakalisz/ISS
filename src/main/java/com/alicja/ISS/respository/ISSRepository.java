package com.alicja.ISS.respository;

import com.alicja.ISS.model.ISS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISSRepository extends JpaRepository<ISS, Long> {
    @Query("SELECT i FROM ISS i WHERE i.timeStamp = ?1 ")
    Optional<ISS> findByTimeStamp(String timeStamp);

}
