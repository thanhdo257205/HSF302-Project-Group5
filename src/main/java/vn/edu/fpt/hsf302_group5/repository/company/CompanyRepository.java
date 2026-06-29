package vn.edu.fpt.hsf302_group5.repository.company;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.hsf302_group5.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
}
