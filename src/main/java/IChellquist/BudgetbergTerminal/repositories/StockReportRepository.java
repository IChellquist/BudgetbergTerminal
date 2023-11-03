package IChellquist.BudgetbergTerminal.repositories;

import IChellquist.BudgetbergTerminal.models.StockReport;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StockReportRepository  extends MongoRepository<StockReport, String> {

}
