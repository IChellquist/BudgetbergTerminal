package IChellquist.BudgetbergTerminal.services;

import IChellquist.BudgetbergTerminal.models.StockReport;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface StockReportRetrievalService {
    List<StockReport> retrieveStockReports(String reportType, List<String> exchanges, List<String> sectors, Date dateSelected);


}
