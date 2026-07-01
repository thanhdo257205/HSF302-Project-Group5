package vn.edu.fpt.hsf302_group5.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.fpt.hsf302_group5.dto.administrativeunit.AdministrativeUnitResponse;
import vn.edu.fpt.hsf302_group5.service.administrativeunit.AdministrativeUnitService;

import java.util.List;

@RestController
@RequestMapping("/api/recruiter")
@RequiredArgsConstructor
public class LoadDistrictApiController {
    private final AdministrativeUnitService unitService;

    @GetMapping("/load-district")
    public ResponseEntity<List<AdministrativeUnitResponse>> loadDistrict(@RequestParam("province_id")Integer provinceId) {
        List<AdministrativeUnitResponse> list = unitService.getByProvinceId(provinceId);
        return ResponseEntity.ok(list);
    }
}
