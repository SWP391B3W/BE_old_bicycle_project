package swp391.old_bicycle_project.dto.ghn;

import lombok.Data;
import java.util.List;

@Data
public class GhnResponse<T> {
    private Integer code;
    private String message;
    private T data;
}
