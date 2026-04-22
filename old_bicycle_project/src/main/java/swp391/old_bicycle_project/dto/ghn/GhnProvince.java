package swp391.old_bicycle_project.dto.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GhnProvince {
    @JsonProperty("ProvinceID")
    private Integer provinceID;
    @JsonProperty("ProvinceName")
    private String provinceName;
    @JsonProperty("Code")
    private String code;
}
