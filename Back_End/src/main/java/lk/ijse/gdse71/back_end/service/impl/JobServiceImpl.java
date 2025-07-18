package lk.ijse.gdse71.back_end.service.impl;

import lk.ijse.gdse71.back_end.dto.JobDTO;
import lk.ijse.gdse71.back_end.entity.Job;
import lk.ijse.gdse71.back_end.repository.JobRepository;
import lk.ijse.gdse71.back_end.service.JobService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final ModelMapper modelMapper;

    @Override
    public void saveJob(JobDTO jobDTO) {
        jobRepository.save(modelMapper.map(jobDTO, Job.class));
    }

    @Override
    public void updateJob(JobDTO jobDTO) {
        jobRepository.save(modelMapper.map(jobDTO, Job.class));
    }

    @Override
    public List<JobDTO> getAllJobs() {
       List<Job> jobs = jobRepository.findAll();
       return modelMapper.map(jobs, new TypeToken<List<JobDTO>>(){}.getType());
    }

    @Override
    public void changeJobStatus(Integer id) {
        jobRepository.updateStatus(id);
    }

    @Override
    public List<JobDTO> getAllJobsByKeyword(String keyword) {
       List<Job> jobs =  jobRepository.findJobByJobTitleContainingIgnoreCase(keyword);
        return modelMapper.map(jobs, new TypeToken<List<JobDTO>>(){}.getType());
    }


}