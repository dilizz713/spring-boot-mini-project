package lk.ijse.gdse71.back_end.service.impl;

import lk.ijse.gdse71.back_end.dto.JobDTO;
import lk.ijse.gdse71.back_end.entity.Job;
import lk.ijse.gdse71.back_end.exceptions.ResourceAlreadyExistsException;
import lk.ijse.gdse71.back_end.exceptions.ResourceNotFoundException;
import lk.ijse.gdse71.back_end.repository.JobRepository;
import lk.ijse.gdse71.back_end.service.JobService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;
    private final ModelMapper modelMapper;

    @Override
    public void saveJob(JobDTO jobDTO) {
        Job job = jobRepository.findByCompanyAndJobTitle(jobDTO.getCompany(), jobDTO.getJobTitle());
        if (job == null) {
            jobRepository.save(modelMapper.map(jobDTO, Job.class));
        }
        else {
            throw new ResourceAlreadyExistsException("Already Exists");
        }

    }

   /* @Override
    public void updateJob(JobDTO jobDTO) {
        jobRepository.save(modelMapper.map(jobDTO, Job.class));
    }*/

   @Override
   public void updateJob(JobDTO jobDTO) {
       /*Job existingJob = jobRepository.findById(jobDTO.getId())
               .orElseThrow(() -> new RuntimeException("Job not found with ID: " + jobDTO.getId()));

       existingJob.setJobTitle(jobDTO.getJobTitle());
       existingJob.setCompany(jobDTO.getCompany());
       existingJob.setLocation(jobDTO.getLocation());
       existingJob.setType(jobDTO.getType());
       existingJob.setJobDescription(jobDTO.getJobDescription());
       existingJob.setStatus(jobDTO.getStatus());

       jobRepository.save(existingJob);*/

       Optional<Job> job = jobRepository.findById(jobDTO.getId());

       if(job.isPresent()){
           Job existingJob = job.get();
           existingJob.setJobTitle(jobDTO.getJobTitle());
           existingJob.setCompany(jobDTO.getCompany());
           existingJob.setLocation(jobDTO.getLocation());
           existingJob.setType(jobDTO.getType());
           existingJob.setJobDescription(jobDTO.getJobDescription());
           existingJob.setStatus(jobDTO.getStatus());

           jobRepository.save(existingJob);
       }else {
           throw new ResourceNotFoundException("Job Not Found");
       }
   }


    @Override
    public List<JobDTO> getAllJobs() {
       List<Job> jobs = jobRepository.findAll();
       if (jobs.isEmpty()) {
           throw new ResourceNotFoundException("No Job Found");    //custom exception   - 500
       }
       //throw new ResourceNotFoundException("No Job Found");    ---- 404
       return modelMapper.map(jobs, new TypeToken<List<JobDTO>>(){}.getType());
    }

    @Override
    public void changeJobStatus(Integer id) {
        /*jobRepository.updateStatus(id);*/
       /* Job job = jobRepository.findById(id).orElseThrow(() -> new RuntimeException("Job not found with ID : " + id));*/
        Optional<Job> job = jobRepository.findById(id);

        if(job.isPresent()){
            Job existingJob = job.get();
            if("Activated".equalsIgnoreCase(existingJob.getStatus())){
                existingJob.setStatus("Deactivated");
            }else{
                existingJob.setStatus("Activated");
            }
            jobRepository.save(existingJob);
        }else{
            throw new ResourceNotFoundException("Job Not Found");
        }

    }

    @Override
    public List<JobDTO> getAllJobsByKeyword(String keyword) {
       List<Job> jobs =  jobRepository.findJobByJobTitleContainingIgnoreCase(keyword);
       if(jobs.isEmpty()){
           throw new ResourceNotFoundException("No Job Found");
       }else{
           return modelMapper.map(jobs, new TypeToken<List<JobDTO>>(){}.getType());
       }

    }

    @Override
    public void deleteJob(int id) {
       Optional<Job> job = jobRepository.findById(id);
       if(job.isPresent()){
           jobRepository.deleteById(id);
       }else{
           throw new ResourceNotFoundException("Job Not Found");
       }

    }

    @Override
    public Page<JobDTO> getPaginatedJobs(int page, int size) {
        Page<Job> jobPage = jobRepository.findAll(PageRequest.of(page, size));
        return jobPage.map(job -> modelMapper.map(job, JobDTO.class));
    }


}