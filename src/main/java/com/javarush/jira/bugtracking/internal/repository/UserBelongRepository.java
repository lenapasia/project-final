package com.javarush.jira.bugtracking.internal.repository;

import com.javarush.jira.bugtracking.internal.model.UserBelong;
import com.javarush.jira.bugtracking.to.ObjectType;
import com.javarush.jira.common.BaseRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface UserBelongRepository extends BaseRepository<UserBelong> {

    boolean existsByObjectIdAndObjectTypeAndUserId(long objectId, ObjectType objectType, long userId);

}
