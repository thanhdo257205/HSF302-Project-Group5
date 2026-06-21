package vn.edu.fpt.hsf302_group5.repository.industry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.fpt.hsf302_group5.dto.industry.IndustryResponse;
import vn.edu.fpt.hsf302_group5.entity.Industry;

import java.util.List;

public interface IndustryRepository extends JpaRepository<Industry, Integer> {

    @Query("""
                        select new vn.edu.fpt.hsf302_group5.dto.industry.IndustryResponse(i.industryId, i.industryName) from Industry i
            """)
    List<IndustryResponse> findAllInsdustryResponse();
}
