package swp391.old_bicycle_project.dto.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

public class GhnFeeResponse {
    private Integer total;
    @JsonProperty("service_fee")
    private Integer serviceFee;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(Integer serviceFee) {
        this.serviceFee = serviceFee;
    }
}
