package com.dbtest.yashwith.core_auth.entities;

import com.dbtest.yashwith.entities.DateAudit;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@EqualsAndHashCode(callSuper = true)
public class RefreshTokenMongo extends DateAudit {
    private String id;
    private String token;
    private String userId;
    private String phoneNumber;
    private String email;
    private Date timeout;
    private Date lastRefreshedAt;
}
