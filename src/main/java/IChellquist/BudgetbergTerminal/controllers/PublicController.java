package IChellquist.BudgetbergTerminal.controllers;

import IChellquist.BudgetbergTerminal.models.StockReport;
import IChellquist.BudgetbergTerminal.services.StockReportRetrievalService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/public")
@AllArgsConstructor
@Getter
@Setter
public class PublicController {
    @Autowired
    StockReportRetrievalService stockReportRetrievalService;

    @GetMapping("/retrievestockreports")
    public ResponseEntity<?> searchForAndReturnStockReports(@RequestParam("reportType") String reportType, @RequestParam("exchanges") List<String> exchanges, @RequestParam("sectors") List<String> sectors, @RequestParam("dateSelected") Date dateSelected, @RequestParam("offset") String offset) {

        try {

            List<StockReport> stockReportsRetrieved = stockReportRetrievalService.retrieveStockReports(reportType,exchanges,sectors,dateSelected, Integer.parseInt(offset));
            return ResponseEntity.ok(stockReportsRetrieved);
        }
        catch(Error e){

        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error retrieving stock reports");


    }


}
