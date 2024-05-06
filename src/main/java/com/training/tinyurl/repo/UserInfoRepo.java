package com.training.tinyurl.repo;

import com.training.tinyurl.entity.UserInfoEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepo extends MongoRepository<UserInfoEntity,String> {
}
