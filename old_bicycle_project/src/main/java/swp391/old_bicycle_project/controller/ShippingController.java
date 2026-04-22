package swp391.old_bicycle_project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import swp391.old_bicycle_project.dto.ghn.*;
import swp391.old_bicycle_project.dto.response.ApiResponse;
import swp391.old_bicycle_project.service.impl.GhnService;

import java.util.List;

@RestController
@RequestMapping("/api/public/shipping")
@RequiredArgsConstructor
public class ShippingController {

    private final GhnService ghnService;

    @jakarta.annotation.PostConstruct
    public void init() {
        System.out.println("SHIPPING CONTROLLER INITIALIZED");
    }

    @GetMapping("/provinces")
    public ApiResponse<List<GhnProvince>> getProvinces() {
        return ApiResponse.<List<GhnProvince>>builder().result(ghnService.getProvinces()).build();
    }

    @GetMapping("/districts")
    public ApiResponse<List<GhnDistrict>> getDistricts(@RequestParam("province_id") Integer provinceId) {
        return ApiResponse.<List<GhnDistrict>>builder().result(ghnService.getDistricts(provinceId)).build();
    }

    @GetMapping("/wards")
    public ApiResponse<List<GhnWard>> getWards(@RequestParam("district_id") Integer districtId) {
        return ApiResponse.<List<GhnWard>>builder().result(ghnService.getWards(districtId)).build();
    }

    @PostMapping("/calculate-fee")
    public ApiResponse<GhnFeeResponse> calculateFee(@RequestBody GhnFeeRequest request) {
        Integer fee = ghnService.calculateFee(request);
        GhnFeeResponse response = new GhnFeeResponse();
        response.setTotal(fee);
        response.setServiceFee(fee);
        return ApiResponse.<GhnFeeResponse>builder().result(response).build();
    }
}
