package com.training.tinyurl.entity;

import com.training.tinyurl.constants.AccountType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "userinfocollection")
public class UserInfoEntity {
    @Id
    private String email;
    private String password;
    private AccountType accountType;
    private int usedQuota;

    public UserInfoEntity(String email, String password, AccountType accountType) {
        this.email = email;
        this.password = password;
        this.accountType = accountType;
    }

}