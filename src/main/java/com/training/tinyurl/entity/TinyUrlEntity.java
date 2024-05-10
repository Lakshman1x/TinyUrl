package com.training.tinyurl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tinyurlcollection")
public class TinyUrlEntity {
    @Id
    private String tinyUrl;
    private String longUrl;
}
