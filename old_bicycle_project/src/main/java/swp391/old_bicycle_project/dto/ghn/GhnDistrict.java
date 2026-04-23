package swp391.old_bicycle_project.dto.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GhnDistrict {
    @JsonProperty("DistrictID")
    private Integer districtId;
    @JsonProperty("DistrictName")
    private String districtName;

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }
}
