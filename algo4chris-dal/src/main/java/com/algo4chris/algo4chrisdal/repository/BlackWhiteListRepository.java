package com.algo4chris.algo4chrisdal.repository;

import com.algo4chris.algo4chrisdal.models.BlackWhiteList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlackWhiteListRepository extends JpaRepository<BlackWhiteList, Long> {

    @Override
    Optional<BlackWhiteList> findById(Long id);

    List<BlackWhiteList> findByType(int type);

}
