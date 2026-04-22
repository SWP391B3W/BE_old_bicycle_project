package swp391.old_bicycle_project.dto.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GhnWard {
    @JsonProperty("WardCode")
    private String wardCode;
    @JsonProperty("DistrictID")
    private Integer districtID;
    @JsonProperty("WardName")
    private String wardName;
}
