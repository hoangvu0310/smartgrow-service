package com.hoang.jobfinder.repository;

import com.hoang.jobfinder.entity.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
  List<Company> findCompanyByCompanyId(Long companyId);
  boolean existsCompanyByCompanyName(String companyName);
}
