package IChellquist.BudgetbergTerminal.services;

import IChellquist.BudgetbergTerminal.models.NewsArticle;
import IChellquist.BudgetbergTerminal.models.ReportTypeEnum;
import IChellquist.BudgetbergTerminal.models.Stock;
import IChellquist.BudgetbergTerminal.models.StockReport;
import IChellquist.BudgetbergTerminal.payload.request.ReportSettingsRequest;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public interface StockReportCreationService {
    void createStockReportsFromPredefinedScan(String reportType, List<String> exchanges, List<String> sectors) throws Exception;
    List<Stock> getStocksForReport(String urlForReport, List<String> exchanges, List<String> sectors) throws Exception;
    List<NewsArticle> getNewsArticleForStock(Stock stock) throws Exception;
    BufferedImage getStockImage(String stockSymbol) throws Exception;

    byte[] convertBufferedImageToByteArray(BufferedImage image) throws Exception;

    List<StockReport> findStockReportsFromReportSettings(ReportSettingsRequest reportSettingsRequest);
}
