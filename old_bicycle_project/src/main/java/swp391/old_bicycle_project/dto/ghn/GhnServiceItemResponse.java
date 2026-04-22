package swp391.old_bicycle_project.dto.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GhnServiceItemResponse {
    @JsonProperty("service_id")
    private Integer serviceId;
    @JsonProperty("short_name")
    private String shortName;
    @JsonProperty("service_type_id")
    private Integer serviceTypeId;
}
