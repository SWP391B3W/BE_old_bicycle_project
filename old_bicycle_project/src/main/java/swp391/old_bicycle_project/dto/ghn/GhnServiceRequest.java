package swp391.old_bicycle_project.dto.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

public class GhnServiceRequest {
    @JsonProperty("shop_id")
    private Integer shopId;
    @JsonProperty("from_district")
    private Integer fromDistrict;
    @JsonProperty("to_district")
    private Integer toDistrict;

    public GhnServiceRequest() {}

    public GhnServiceRequest(Integer shopId, Integer fromDistrict, Integer toDistrict) {
        this.shopId = shopId;
        this.fromDistrict = fromDistrict;
        this.toDistrict = toDistrict;
    }

    public static GhnServiceRequestBuilder builder() {
        return new GhnServiceRequestBuilder();
    }

    public Integer getShopId() { return shopId; }
    public void setShopId(Integer shopId) { this.shopId = shopId; }
    public Integer getFromDistrict() { return fromDistrict; }
    public void setFromDistrict(Integer fromDistrict) { this.fromDistrict = fromDistrict; }
    public Integer getToDistrict() { return toDistrict; }
    public void setToDistrict(Integer toDistrict) { this.toDistrict = toDistrict; }

    public static class GhnServiceRequestBuilder {
        private Integer shopId;
        private Integer fromDistrict;
        private Integer toDistrict;
        public GhnServiceRequestBuilder shopId(Integer shopId) { this.shopId = shopId; return this; }
        public GhnServiceRequestBuilder fromDistrict(Integer fromDistrict) { this.fromDistrict = fromDistrict; return this; }
        public GhnServiceRequestBuilder toDistrict(Integer toDistrict) { this.toDistrict = toDistrict; return this; }
        public GhnServiceRequest build() { return new GhnServiceRequest(shopId, fromDistrict, toDistrict); }
    }
}
