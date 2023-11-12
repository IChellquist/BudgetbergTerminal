package IChellquist.BudgetbergTerminal.services.implementations;

import IChellquist.BudgetbergTerminal.models.StockReport;
import IChellquist.BudgetbergTerminal.services.StockReportRetrievalService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Getter
@Setter
public class StockReportRetrievalServiceImpl implements StockReportRetrievalService {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Override
    public List<StockReport> retrieveStockReports(String reportType, List<String> exchanges, List<String> sectors, Date dateSelected) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateSelected);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date startOfDay = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date beginOfDay = cal.getTime();

        Criteria criteria = new Criteria().andOperator(
                Criteria.where("reportType").is(reportType),
                Criteria.where("reportCreationDate").gte(startOfDay).lte(beginOfDay),
                Criteria.where("stock.exchange").in(exchanges),
                Criteria.where("stock.sector").in(sectors));

        Query query = new Query(criteria);
        return mongoTemplate.find(query, StockReport.class);
    }
}
