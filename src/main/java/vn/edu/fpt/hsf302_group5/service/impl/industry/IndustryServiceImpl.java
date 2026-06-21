package vn.edu.fpt.hsf302_group5.service.impl.industry;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.fpt.hsf302_group5.dto.industry.IndustryResponse;
import vn.edu.fpt.hsf302_group5.repository.industry.IndustryRepository;
import vn.edu.fpt.hsf302_group5.service.industry.IndustryService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class IndustryServiceImpl implements IndustryService {

    private final IndustryRepository industryRepository;

    @Override
    public List<IndustryResponse> getAllIndustryResponse() {
        return industryRepository.findAllInsdustryResponse();
    }
}
