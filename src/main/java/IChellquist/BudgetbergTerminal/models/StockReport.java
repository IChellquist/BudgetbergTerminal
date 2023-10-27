package IChellquist.BudgetbergTerminal.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection="StockReports")
public class StockReport {
    String reportType;
    Date reportCreationDate;
    Stock stock;
    List<NewsArticle> newsArticlesList;
}
