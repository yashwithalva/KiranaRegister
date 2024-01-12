package com.dbtest.yashwith.model.transaction;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportDTO {
    private double netFlow;
    private double totalDebit;
    private double totalCredit;
}
