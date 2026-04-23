package swp391.old_bicycle_project.dto.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GhnWard {
    @JsonProperty("WardCode")
    private String wardCode;
    @JsonProperty("DistrictID")
    private Integer districtID;
    @JsonProperty("WardName")
    private String wardName;

    public String getWardCode() {
        return wardCode;
    }

    public void setWardCode(String wardCode) {
        this.wardCode = wardCode;
    }

    public Integer getDistrictID() {
        return districtID;
    }

    public void setDistrictID(Integer districtID) {
        this.districtID = districtID;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }
}
