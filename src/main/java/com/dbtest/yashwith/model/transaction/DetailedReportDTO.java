package com.dbtest.yashwith.model.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailedReportDTO {
    private double totalCredit;
    private double totalDebit;
    // Below maps for early monthly and annually.
    // TODO: Map<Currency, Amount> : Highest to lowest credit based on the currency.
    // TODO: Map<UserId, Amount> : Highest to lowest credit earned based on the user.

}
