package IChellquist.BudgetbergTerminal.services.implementations;

import IChellquist.BudgetbergTerminal.models.NewsArticle;
import IChellquist.BudgetbergTerminal.models.ReportTypeEnum;
import IChellquist.BudgetbergTerminal.models.Stock;
import IChellquist.BudgetbergTerminal.models.StockReport;
import IChellquist.BudgetbergTerminal.payload.request.ReportSettingsRequest;
import IChellquist.BudgetbergTerminal.repositories.StockReportRepository;
import IChellquist.BudgetbergTerminal.services.StockReportCreationService;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientCodecCustomizer;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Getter
@Setter
@Slf4j
@EnableScheduling
public class StockReportCreationServiceImpl implements StockReportCreationService {

    @Value("${gainer_table_url}")
    String strongVolumeGainersUrl;
    @Value("${azure_api_header}")
    String azureHttpHeader;
    @Value("${azure_api_key}")
    String azureApiKey;
    @Value("${base_stockimage_url}")
    String baseStockImageUrl;

    private final WebClient webClient;
    private final HttpClient httpClient = HttpClients.createDefault();

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private StockReportRepository stockReportRepository;

    StockReportCreationServiceImpl(){
        this.webClient = new WebClient();
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setCssEnabled(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
    }


    @Scheduled(cron = "0 0 17 ? * MON-FRI")
    public void scheduledDefaultStockReportCreation() throws Exception {
        String reportType = "StrongVolumeGainers";
        List<String> exchanges = new ArrayList<String>(Arrays.asList(new String[]{"AMEX", "NASD", "NYSE", "OTCMKT"}));
        List<String> sectors = new ArrayList<String>(Arrays.asList(new String[]{"Communication Services", "Consumer Discretionary", "Energy", "Financial", "Health Care", "Industrial", "Materials", "Technology", "Not Specified"}));
        createStockReportsFromPredefinedScan(reportType, exchanges, sectors);
        log.info("Scheduled CRON stock report creation successful at 5PM on a weekday");
    }


    @Override
    public void createStockReportsFromPredefinedScan(String reportType, List<String> exchanges, List<String> sectors) throws Exception {

        String urlForReport = "";
        if (reportType.equals("StrongVolumeGainers")){
            urlForReport = strongVolumeGainersUrl;
        }

        List<Stock> stocks = getStocksForReport(urlForReport, exchanges, sectors);

        for (Stock stock : stocks){
            try {
                //put the thread to sleep as to not overload the source of images with requests
                Random randomGen = new Random();
                int upper = 5, lower = 1;
                int random = randomGen.nextInt(upper-lower) + lower;
                Thread.sleep(1000*random);

                List<NewsArticle> newsArticleList = getNewsArticleForStock(stock);
                BufferedImage stockImage = getStockImage(stock.getSymbol());

                StockReport newStockReport = new StockReport();
                newStockReport.setReportType(reportType);
                newStockReport.setReportCreationDate(new Date());
                newStockReport.setStock(stock);
                newStockReport.setNewsArticlesList(newsArticleList);
                newStockReport.setReportImage(convertBufferedImageToByteArray(stockImage));

                stockReportRepository.save(newStockReport);
                log.info("Stock Report for " + stock.getSymbol() + " saved");


            }
            catch (Exception e){
                log.error(e.getMessage());
                log.error("Error creating stock report for stock " + stock.getSymbol() );

            }
        }
        log.info("Stock report creation request complete");
        }


    @Override
    public List<Stock> getStocksForReport(String urlForReport, List<String> exchanges, List<String> sectors) throws Exception {

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

        List<Stock> filteredStocks = new ArrayList<>();

        //filter the stocks for exchanges and sectors. First row is garbage, discard
        for (int i = 1; i < stocks.size(); i++){
            Stock stock = stocks.get(i);
            String exchange = stock.getExchange();
            String sector = stock.getSector();
            if (exchanges.contains(exchange) && sectors.contains(sector)){
                filteredStocks.add(stock);
            }

        }
        return filteredStocks;
    }

    @Override
    public List<NewsArticle> getNewsArticleForStock(Stock stock) throws Exception {
        URIBuilder builder = new URIBuilder("https://api.bing.microsoft.com/v7.0/news/search");
        //Sets the parameters for the search
        builder.setParameter("q", stock.getName());
        builder.setParameter("count", "10"); //The number of news articles returned
        builder.setParameter("offset", "0");
        builder.setParameter("mkt", "en-us"); //The language that will be returned
        builder.setParameter("safeSearch", "Moderate"); //Prevents my search results from being crowded by porn.
        String DEBUGheader = azureHttpHeader;
        String DEBUGApiKey = azureApiKey;

        //do the news searches
        URI uri = builder.build();
        HttpGet getRequest = new HttpGet(uri);
        getRequest.setHeader(azureHttpHeader, azureApiKey);
        HttpResponse httpResponse = httpClient.execute(getRequest);
        HttpEntity httpEntity = httpResponse.getEntity();
        String results = null;
        if (httpEntity != null){results = EntityUtils.toString(httpEntity);}
        //parse the news searches
        JSONObject jsonObject = new JSONObject(results);
        JSONArray jsonNewsArticles = jsonObject.getJSONArray("value");
        List<NewsArticle> newsArticleList = new ArrayList<>();

        for (int i = 0; i < jsonNewsArticles.length(); i++){
            NewsArticle newsArticle = new NewsArticle();
            JSONObject jsonNewsArticle = jsonNewsArticles.getJSONObject(i);
            newsArticle.setTitle(jsonNewsArticle.getString("name"));
            newsArticle.setUrl(jsonNewsArticle.getString("url"));
            newsArticle.setText(jsonNewsArticle.getString("description"));
            newsArticle.setDate(Date.from(Instant.parse(jsonNewsArticle.getString("datePublished"))));
            newsArticleList.add(newsArticle);
        }
        return newsArticleList;
    }

    @Override
    public BufferedImage getStockImage(String stockSymbol) throws Exception {
        HtmlPage page = webClient.getPage(baseStockImageUrl + stockSymbol);
        HtmlImage image = page.getHtmlElementById("chartImg");
        return image.getImageReader().read(0);
    }

    @Override
    public byte[] convertBufferedImageToByteArray(BufferedImage image) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        return baos.toByteArray();
    }

    @Override
    public List<StockReport> findStockReportsFromReportSettings(ReportSettingsRequest reportSettingsRequest) {
        String reportType = reportSettingsRequest.getReportType();
        List<String> exchanges = reportSettingsRequest.getExchanges();
        List<String> sectors = reportSettingsRequest.getSectors();
        Date date = reportSettingsRequest.getDateSelected();


        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

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
