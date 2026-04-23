package swp391.old_bicycle_project.dto.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

public class GhnServiceItemResponse {
    @JsonProperty("service_id")
    private Integer serviceId;
    @JsonProperty("short_name")
    private String shortName;
    @JsonProperty("service_type_id")
    private Integer serviceTypeId;

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public Integer getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Integer serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }
}
