package com.hoang.jobfinder.repository;

import com.hoang.jobfinder.entity.company.DescriptionTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DescriptionTagRepository extends JpaRepository<DescriptionTag, Long> {
}
