package lk.ijse.gdse71.back_end.service;

import lk.ijse.gdse71.back_end.dto.JobDTO;
import lk.ijse.gdse71.back_end.entity.Job;
import org.hibernate.query.Page;

import java.util.List;

public interface JobService {
     void saveJob(JobDTO jobDTO);
     void updateJob(JobDTO jobDTO);
     List<JobDTO> getAllJobs();
     void changeJobStatus(Integer id);
     List<JobDTO> getAllJobsByKeyword(String keyword);

    void deleteJob(int id);


}