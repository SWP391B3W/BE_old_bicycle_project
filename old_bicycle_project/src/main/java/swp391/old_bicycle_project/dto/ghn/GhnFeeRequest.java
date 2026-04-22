package swp391.old_bicycle_project.dto.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GhnFeeRequest {
    @JsonProperty("service_id")
    private Integer serviceId = 53320; // Default service
    
    @JsonProperty("service_type_id")
    private Integer serviceTypeId = 2; // E-commerce Standard

    @JsonProperty("from_district_id")
    private Integer fromDistrictId;

    @JsonProperty("from_ward_code")
    private String fromWardCode;

    @JsonProperty("to_district_id")
    private Integer toDistrictId;
    
    @JsonProperty("to_ward_code")
    private String toWardCode;

    private Integer weight = 15000;
    private Integer length = 150;
    private Integer width = 20;
    private Integer height = 80;

    @JsonProperty("insurance_value")
    private Integer insuranceValue = 10000; // Fake default insurance
}
