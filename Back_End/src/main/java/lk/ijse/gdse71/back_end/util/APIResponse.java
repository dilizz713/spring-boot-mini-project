package lk.ijse.gdse71.back_end.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class APIResponse {
    private int status;
    private String message;
    private Object data;
}
