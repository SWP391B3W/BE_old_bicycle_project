package swp391.old_bicycle_project.dto.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GhnDistrict {
    @JsonProperty("DistrictID")
    private Integer districtID;
    @JsonProperty("ProvinceID")
    private Integer provinceID;
    @JsonProperty("DistrictName")
    private String districtName;
    @JsonProperty("Code")
    private String code;
}
