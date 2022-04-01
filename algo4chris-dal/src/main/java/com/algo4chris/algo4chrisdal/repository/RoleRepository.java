package com.algo4chris.algo4chrisdal.repository;

import com.algo4chris.algo4chrisdal.models.ERole;
import com.algo4chris.algo4chrisdal.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(ERole name);

    Optional<Role> findById(Integer id);
}