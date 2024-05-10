package com.training.tinyurl.repo;

import com.training.tinyurl.entity.TinyUrlEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TinyUrlRepo extends MongoRepository<TinyUrlEntity,String> {
    Optional<TinyUrlEntity> findByLongUrl(String longUrl);
}
