package swp391.old_bicycle_project.dto.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GhnServiceRequest {
    @JsonProperty("shop_id")
    private Integer shopId;
    @JsonProperty("from_district")
    private Integer fromDistrict;
    @JsonProperty("to_district")
    private Integer toDistrict;
}
