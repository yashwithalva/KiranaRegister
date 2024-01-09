package com.dbtest.yashwith.entities;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public abstract class DateAudit implements Serializable {

    @CreatedDate private Date createdAt;

    @LastModifiedDate private Date updatedAt;
}
