package IChellquist.BudgetbergTerminal.controllers;


import IChellquist.BudgetbergTerminal.models.StockReport;
import IChellquist.BudgetbergTerminal.payload.request.ReportSettingsRequest;
import IChellquist.BudgetbergTerminal.payload.response.ReportListResponse;
import IChellquist.BudgetbergTerminal.services.StockReportCreationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/admin")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdminFunctionsController {

    @Autowired
    StockReportCreationService stockReportCreationService;




@PostMapping("/testBackendReportGeneration")
public ResponseEntity<?> testBackendReportGeneration(@Valid @RequestBody ReportSettingsRequest reportSettingsRequest) throws Exception {
    //preprocess frontend data before passing onto the service method
    String reportType = reportSettingsRequest.getReportType();
    List<String> exchanges = reportSettingsRequest.getExchanges();
    List<String> sectors = reportSettingsRequest.getSectors();
    sectors.replaceAll(sector -> sector.equals("Not Specified") ? "" : sector);
    stockReportCreationService.createStockReportsFromPredefinedScan(reportType, exchanges, sectors);
    return ResponseEntity.ok(null);
}

@PostMapping("/testBackendQuery")
public ResponseEntity<?> testBackendQuery(@Valid @RequestBody ReportSettingsRequest reportSettingsRequest) throws Exception {

    List<StockReport> stockReportList = stockReportCreationService.findStockReportsFromReportSettings(reportSettingsRequest);
    ReportListResponse reportListResponse = new ReportListResponse();
    reportListResponse.setStockReports(stockReportList);
    return ResponseEntity.ok(reportListResponse);
}



}
