package IChellquist.BudgetbergTerminal.services.implementations;

import IChellquist.BudgetbergTerminal.models.NewsArticle;
import IChellquist.BudgetbergTerminal.models.ReportTypeEnum;
import IChellquist.BudgetbergTerminal.models.Stock;
import IChellquist.BudgetbergTerminal.models.StockReport;
import IChellquist.BudgetbergTerminal.services.StockReportCreationService;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientCodecCustomizer;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Getter
@Setter
public class StockReportCreationServiceImpl implements StockReportCreationService {

    @Value("${gainer_table_url}")
    String strongVolumeGainersUrl;
    @Value("${azure_api_header}")
    String azureHttpHeader;
    @Value("${azure_api_key}")
    String azureApiKey;

    private final WebClient webClient;
    private final HttpClient httpClient = HttpClients.createDefault();

    StockReportCreationServiceImpl(){
        this.webClient = new WebClient();
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
    }


    @Override
    public void createStockReportsFromPredefinedScan(String reportType, List<String> exchanges, List<String> sectors) throws Exception {
        String urlForReport = "";
        if (reportType.equals("StrongVolumeGainers")){
            urlForReport = strongVolumeGainersUrl;
        }
        //get the Table for the specific reportType, then get the rows
        HtmlPage page = webClient.getPage(urlForReport);
        HtmlTable table = page.getHtmlElementById("sccDataTable");
        List<HtmlTableRow> htmlTableRowList = table.getRows();
        List<Stock> stocks = new ArrayList<>();

        for (HtmlTableRow row : htmlTableRowList){
            Stock stock = new Stock(row.getCell(
                    2).asNormalizedText(),
                    row.getCell(1).asNormalizedText(),
                    row.getCell(3).asNormalizedText(),
                    row.getCell(4).asNormalizedText(),
                    row.getCell(5).asNormalizedText()
            );
            stocks.add(stock);
        }

        stocks.stream().filter(stock -> exchanges.contains(stock.getExchange()) && sectors.contains(stock.getSector())).collect(Collectors.toList());

        for (Stock stock : stocks){
            StockReport stockReport = new StockReport();
            stockReport.setStock(stock);
            URIBuilder builder = new URIBuilder("https://api.cognitive.microsoft.com/bing/v7.0/news/search");
            //Sets the parameters for the search
            builder.setParameter("q", stock.getName());
            builder.setParameter("count", "10"); //The number of news articles returned
            builder.setParameter("offset", "0");
            builder.setParameter("mkt", "en-us"); //The language that will be returned
            builder.setParameter("safeSearch", "Moderate"); //Prevents my search results from being crowded by porn.

            URI uri = builder.build();
            HttpGet getRequest = new HttpGet(uri);
            getRequest.setHeader(azureHttpHeader, azureApiKey);
            HttpResponse httpResponse = httpClient.execute(getRequest);
            HttpEntity httpEntity = httpResponse.getEntity();
            String results = null;
            if (httpEntity != null){results = EntityUtils.toString(httpEntity);}
            else {continue;}

            JSONObject jsonObject = new JSONObject(results);
            JSONArray jsonNewsArticles = jsonObject.getJSONArray("value");
            List<NewsArticle> newsArticleList = new ArrayList<>();
            for (int i = 0; i < jsonNewsArticles.length(); i++){
                NewsArticle newsArticle = new NewsArticle();
                JSONObject jsonNewsArticle = jsonNewsArticles.getJSONObject(i);
                newsArticle.setTitle(jsonNewsArticle.getString("name"));
                newsArticle.setUrl(jsonNewsArticle.getString("url"));
                newsArticle.setText(jsonNewsArticle.getString("description"));
                newsArticle.setDate(new Date(jsonNewsArticle.getString("datePublished")));
                newsArticleList.add(newsArticle);
            }
            stockReport.setNewsArticlesList(newsArticleList);

        }

























    }


}
