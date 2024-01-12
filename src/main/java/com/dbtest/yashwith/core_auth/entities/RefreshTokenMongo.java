package com.dbtest.yashwith.core_auth.entities;

import com.dbtest.yashwith.entities.DateAudit;
import java.util.Date;
import javax.persistence.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("refresh-token")
@EqualsAndHashCode(callSuper = true)
public class RefreshTokenMongo extends DateAudit {
    @Id private String id;

    @Indexed(unique = true)
    private String token;

    private String userId;
    private String phoneNumber;
    private String email;
    private Date timeout;
    private Date lastRefreshedAt;
}
