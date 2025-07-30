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
    @Pattern(regexp = "^[\\p{L}0-9 .,'&()/-]{2,100}$", message = "Job title contains invalid characters")
    private String jobTitle;

    @NotBlank(message = "Company name is mandatory")
    @Pattern( regexp = "^[A-Za-z0-9&.,'\\-/() ]{2,100}$",
            message = "Company name can contain letters, numbers, spaces, and basic special characters like & , . ' - / ( )")
    private String company;

    @NotBlank(message = "Location is mandatory")
    private String location;

    @NotNull(message = "Type is mandatory")
    private String type;

    @Size(min = 10, max = 100, message = "Job description must be between 10 and 100 characters")
    private String jobDescription;

    private String status;


    // we can use both of this  for not null -  @NotNull , @NotBlank
}
