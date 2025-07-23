package lk.ijse.gdse71.back_end.controller;

import lk.ijse.gdse71.back_end.dto.JobDTO;
import lk.ijse.gdse71.back_end.service.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/v1/job")
@RestController
@RequiredArgsConstructor
@CrossOrigin
public class JobController {

    //constructor injection
    private final JobService jobService;

    @PostMapping("create")
    public void createJob(@RequestBody JobDTO jobDTO){
        System.out.println(jobDTO);
        jobService.saveJob(jobDTO);
    }


    @PutMapping("update")
    public void  updateJob(@RequestBody JobDTO jobDTO) {
        jobService.updateJob(jobDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteJob(@PathVariable int id) {
        jobService.deleteJob(id);
    }



    @GetMapping("getall")
    public List<JobDTO> getAllJob(){
      return jobService.getAllJobs();
    }

    @PatchMapping("status/{id}")
    public void changeStatus(@PathVariable("id") Integer id){
        jobService.changeJobStatus(id);
    }

    @GetMapping("search/{keyword}")
    public List<JobDTO> searchJob(@PathVariable("keyword") String keyword){
        return jobService.getAllJobsByKeyword(keyword);
    }

    @GetMapping("paginated")
    public Page<JobDTO> getPaginatedJobs(@RequestParam(defaultValue = "0") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        return jobService.getPaginatedJobs(page, size);
    }




}