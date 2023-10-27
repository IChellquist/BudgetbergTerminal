package IChellquist.BudgetbergTerminal.services;

import IChellquist.BudgetbergTerminal.models.NewsArticle;
import IChellquist.BudgetbergTerminal.models.ReportTypeEnum;
import IChellquist.BudgetbergTerminal.models.Stock;

import java.util.List;

public interface StockReportCreationService {
    void createStockReportsFromPredefinedScan(String reportType, List<String> exchanges, List<String> sectors) throws Exception;

}
