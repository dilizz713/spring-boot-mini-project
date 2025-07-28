package lk.ijse.gdse71.back_end.controller;

import jakarta.validation.Valid;
import lk.ijse.gdse71.back_end.dto.JobDTO;
import lk.ijse.gdse71.back_end.service.JobService;
import lk.ijse.gdse71.back_end.util.APIResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RequestMapping("api/v1/job")
@RestController
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class JobController {

    //constructor injection
    private final JobService jobService;

    @PostMapping("create")
    public ResponseEntity<APIResponse> createJob(@Valid @RequestBody JobDTO jobDTO){   //bean validations
        log.info("INFO - Job created");
        log.debug("DEBUG - Job created");
        log.error("ERROR - Job created");
        log.warn("WARN - Job created");
        log.trace("TRACE - Job created");
       jobService.saveJob(jobDTO);
       return new ResponseEntity<>(new APIResponse(201,"Saved Successfully",jobDTO), HttpStatus.CREATED);
    }


    @PutMapping("update")
    public ResponseEntity<APIResponse>  updateJob(@RequestBody JobDTO jobDTO) {
        jobService.updateJob(jobDTO);
        return ResponseEntity.ok(new APIResponse(200,"Updated Successfully", jobDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse> deleteJob(@PathVariable int id) {
        jobService.deleteJob(id);
       return ResponseEntity.ok(new APIResponse(200,"Deleted Successfully",null));
    }

    @GetMapping("getall")
    public ResponseEntity<APIResponse> getAllJob(){
        List<JobDTO> jobDTOS = jobService.getAllJobs();
        return ResponseEntity.ok(new APIResponse(200,"Get All Successfully",jobDTOS));

    }

    @PatchMapping("status/{id}")
    public ResponseEntity<APIResponse> changeStatus(@PathVariable("id") Integer id){
        jobService.changeJobStatus(id);
        return ResponseEntity.ok(new APIResponse(200,"Changed Status Successfully",null));
    }

    @GetMapping("search/{keyword}")
    public ResponseEntity<APIResponse> searchJob(@PathVariable("keyword") String keyword){
        List<JobDTO> jobDTOS =  jobService.getAllJobsByKeyword(keyword);
        return ResponseEntity.ok(new APIResponse(200,"Search Successfully",jobDTOS));
    }

    @GetMapping("paginated")
    public ResponseEntity<APIResponse> getPaginatedJobs(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<JobDTO> jobDTOS =  jobService.getPaginatedJobs(page, size);
        return ResponseEntity.ok(new APIResponse(200,"Paginated Successfully",jobDTOS));
    }

}