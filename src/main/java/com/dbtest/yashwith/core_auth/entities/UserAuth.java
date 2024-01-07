package com.dbtest.yashwith.core_auth.entities;

import com.dbtest.yashwith.entities.DateAudit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/** Most probably was used for multiple apps inside JAR. */
@Data
@Document
@EqualsAndHashCode(callSuper = true)
@CompoundIndexes({
    @CompoundIndex(def = "{'phoneNumber':1, 'countryCode':1}", name = "phoneNumber_countryCode")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAuth extends DateAudit {
    @Id private String id;

    private String email;

    private String internalEmail; // We want to implement oauth2.0 for CRM users

    @Indexed(unique = true)
    private String phoneNumber;

    private String countryCode;
}
