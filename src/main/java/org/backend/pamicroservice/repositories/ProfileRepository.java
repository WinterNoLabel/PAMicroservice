package org.backend.pamicroservice.repositories;

import jakarta.transaction.Transactional;
import org.backend.pamicroservice.models.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @Query(value = """
    SELECT * FROM profiles 
    WHERE user_id = :user_id""",
            nativeQuery = true
    )
    Profile findByUserId(@Param("user_id") Long user_id);

}
