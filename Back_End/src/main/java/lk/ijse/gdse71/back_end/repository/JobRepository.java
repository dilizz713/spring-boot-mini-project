package lk.ijse.gdse71.back_end.repository;

import jakarta.transaction.Transactional;
import lk.ijse.gdse71.back_end.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {
  /*  @Modifying
    @Transactional
    @Query(value = "UPDATE Job SET status='Deactivated' WHERE id=?1", nativeQuery = true)
    void updateStatus(int id);*/

    List<Job> findJobByJobTitleContainingIgnoreCase(String keyword);
}
