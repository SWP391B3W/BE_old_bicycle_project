package swp391.old_bicycle_project.dto.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

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

    public Integer getServiceId() { return serviceId; }
    public void setServiceId(Integer serviceId) { this.serviceId = serviceId; }
    public Integer getServiceTypeId() { return serviceTypeId; }
    public void setServiceTypeId(Integer serviceTypeId) { this.serviceTypeId = serviceTypeId; }
    public Integer getFromDistrictId() { return fromDistrictId; }
    public void setFromDistrictId(Integer fromDistrictId) { this.fromDistrictId = fromDistrictId; }
    public String getFromWardCode() { return fromWardCode; }
    public void setFromWardCode(String fromWardCode) { this.fromWardCode = fromWardCode; }
    public Integer getToDistrictId() { return toDistrictId; }
    public void setToDistrictId(Integer toDistrictId) { this.toDistrictId = toDistrictId; }
    public String getToWardCode() { return toWardCode; }
    public void setToWardCode(String toWardCode) { this.toWardCode = toWardCode; }
    public Integer getWeight() { return weight; }
    public void setWeight(Integer weight) { this.weight = weight; }
    public Integer getLength() { return length; }
    public void setLength(Integer length) { this.length = length; }
    public Integer getWidth() { return width; }
    public void setWidth(Integer width) { this.width = width; }
    public Integer getHeight() { return height; }
    public void setHeight(Integer height) { this.height = height; }
    public Integer getInsuranceValue() { return insuranceValue; }
    public void setInsuranceValue(Integer insuranceValue) { this.insuranceValue = insuranceValue; }
}
