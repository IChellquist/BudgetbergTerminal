package IChellquist.BudgetbergTerminal.payload.response;

import IChellquist.BudgetbergTerminal.models.StockReport;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportListResponse {
    List<StockReport> stockReports;
}
