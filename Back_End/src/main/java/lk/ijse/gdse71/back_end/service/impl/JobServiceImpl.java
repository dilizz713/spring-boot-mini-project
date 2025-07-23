package lk.ijse.gdse71.back_end.service.impl;

import lk.ijse.gdse71.back_end.dto.JobDTO;
import lk.ijse.gdse71.back_end.entity.Job;
import lk.ijse.gdse71.back_end.repository.JobRepository;
import lk.ijse.gdse71.back_end.service.JobService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

   /* @Override
    public void updateJob(JobDTO jobDTO) {
        jobRepository.save(modelMapper.map(jobDTO, Job.class));
    }*/
   @Override
   public void updateJob(JobDTO jobDTO) {
       Job existingJob = jobRepository.findById(jobDTO.getId())
               .orElseThrow(() -> new RuntimeException("Job not found with ID: " + jobDTO.getId()));

       existingJob.setJobTitle(jobDTO.getJobTitle());
       existingJob.setCompany(jobDTO.getCompany());
       existingJob.setLocation(jobDTO.getLocation());
       existingJob.setType(jobDTO.getType());
       existingJob.setJobDescription(jobDTO.getJobDescription());
       existingJob.setStatus(jobDTO.getStatus());

       jobRepository.save(existingJob);
   }


    @Override
    public List<JobDTO> getAllJobs() {
       List<Job> jobs = jobRepository.findAll();
       return modelMapper.map(jobs, new TypeToken<List<JobDTO>>(){}.getType());
    }

    @Override
    public void changeJobStatus(Integer id) {
        /*jobRepository.updateStatus(id);*/
        Job job = jobRepository.findById(id).orElseThrow(() -> new RuntimeException("Job not found with ID : " + id));

        if("Activated".equalsIgnoreCase(job.getStatus())){
            job.setStatus("Deactivated");
        }else{
            job.setStatus("Activated");
        }
        jobRepository.save(job);
    }

    @Override
    public List<JobDTO> getAllJobsByKeyword(String keyword) {
       List<Job> jobs =  jobRepository.findJobByJobTitleContainingIgnoreCase(keyword);
        return modelMapper.map(jobs, new TypeToken<List<JobDTO>>(){}.getType());
    }

    @Override
    public void deleteJob(int id) {
        jobRepository.deleteById(id);
    }

    @Override
    public Page<JobDTO> getPaginatedJobs(int page, int size) {
        Page<Job> jobPage = jobRepository.findAll(PageRequest.of(page, size));
        return jobPage.map(job -> modelMapper.map(job, JobDTO.class));
    }


}