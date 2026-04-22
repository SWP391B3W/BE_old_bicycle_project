package swp391.old_bicycle_project.dto.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GhnFeeResponse {
    private Integer total;
    @JsonProperty("service_fee")
    private Integer serviceFee;
}
