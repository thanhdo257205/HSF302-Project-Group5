package vn.edu.fpt.hsf302_group5.service.impl.province;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.fpt.hsf302_group5.dto.province.ProvinceResponse;
import vn.edu.fpt.hsf302_group5.repository.province.ProvinceRepository;
import vn.edu.fpt.hsf302_group5.service.province.ProvinceService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProvinceServiceImpl implements ProvinceService {

    private final ProvinceRepository provinceRepository;

    @Override
    public List<ProvinceResponse> getListProvinceResponse() {
        return provinceRepository.getAllProvinceResponse();
    }
}
