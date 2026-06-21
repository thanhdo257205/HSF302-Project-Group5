package vn.edu.fpt.hsf302_group5.repository.province;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.fpt.hsf302_group5.dto.province.ProvinceResponse;
import vn.edu.fpt.hsf302_group5.entity.Province;

import java.util.List;

public interface ProvinceRepository extends JpaRepository<Province, Integer> {

    @Query("""
        select new vn.edu.fpt.hsf302_group5.dto.province.ProvinceResponse(p.provinceId, p.provinceName, p.provinceCode) from Province p
""")
    List<ProvinceResponse> getAllProvinceResponse();
}
