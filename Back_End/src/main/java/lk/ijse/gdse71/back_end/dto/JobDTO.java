package lk.ijse.gdse71.back_end.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JobDTO {
    private Integer id;

    @NotBlank(message = "Job title is mandatory")
    private String jobTitle;

    @NotBlank(message = "Company name is mandatory")
    @Pattern(regexp = "^[a-zA-Z ]+$" , message = "Company name should contain only alphabets" )   //we can use regex in here
    private String company;

    private String location;

    @NotNull(message = "Type is mandatory")
    private String type;

    @Size(min =  10 , max = 100, message = "Job description at least 10 characters long")    // we can give range of characters
    private String jobDescription;
    private String status;


    // we can use both of this  for not null -  @NotNull , @NotBlank
}
