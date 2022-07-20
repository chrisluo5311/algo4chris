package com.algo4chris.algo4chrisdal.repository;

import com.algo4chris.algo4chrisdal.models.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberName(String userName);

    Optional<Member> findByEmail(String email);

    Boolean existsByMemberName(String userName);

    Boolean existsByEmail(String email);

}