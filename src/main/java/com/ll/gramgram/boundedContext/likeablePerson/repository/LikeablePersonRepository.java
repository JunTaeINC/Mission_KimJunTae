package com.ll.gramgram.boundedContext.likeablePerson.repository;

import com.ll.gramgram.boundedContext.instaMember.entity.InstaMember;
import com.ll.gramgram.boundedContext.likeablePerson.entity.LikeablePerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeablePersonRepository extends JpaRepository<LikeablePerson, Long> {
    List<LikeablePerson> findByFromInstaMemberId(Long fromInstaMemberId);

    //Optional<LikeablePerson> findByFromInstaMemberAndToInstaMember_username(InstaMember instaMember, String username);

    LikeablePerson findByFromInstaMemberAndToInstaMember_username(InstaMember instaMember, String username);

    List<LikeablePerson> findByToInstaMember_username(String username);

    LikeablePerson findByFromInstaMemberIdAndToInstaMember_username(long idNumber, String username);
}
