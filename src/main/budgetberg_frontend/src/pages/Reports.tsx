/* eslint-disable jsx-a11y/alt-text */
import { Flex, Modal } from "antd";
import {
  CenterPopup,
  Collapse,
  Divider,
  InfiniteScroll,
  List,
  Popover,
  Space,
} from "antd-mobile";

import { useState } from "react";
import { useSelector } from "react-redux";
type Stock = {
  name: String;
  symbol: String;
  exchange: String;
  sector: String;
  industry: String;
};

type NewsArticle = {
  title: String;
  url: String;
  text: String;
  date: String;
};

type StockReport = {
  id: String;
  reportType: String;
  reportCreationDate: Date;
  stock: Stock;
  newsArticlesList: NewsArticle[];
  reportImage: Uint8Array;
};

const Reports: React.FC = () => {
  const exchangesSlice = useSelector(
    (state: any) => state.reportSettings.exchanges
  );
  const sectorsSlice = useSelector(
    (state: any) => state.reportSettings.sectors
  );
  const reportTypeSlice = useSelector(
    (state: any) => state.reportSettings.reportType
  );
  const selectedDateSlice = useSelector(
    (state: any) => state.reportSettings.dateSelected
  );

  const [stockReportForPopUp, setStockReportForPopUp] = useState<StockReport>();
  const [popUpVisible, setPopUpVisibile] = useState(false);

  const [stockReportList, setStockReportList] = useState<StockReport[]>([]);
  const [hasMore, setHasMore] = useState(true);

  async function retrieveMoreStockReports() {
    const queryUrl = new URL(
      process.env.REACT_APP_API_URL + "/api/public/retrievestockreports"
    );
    queryUrl.searchParams.set("reportType", reportTypeSlice);
    queryUrl.searchParams.set("exchanges", exchangesSlice);
    queryUrl.searchParams.set("sectors", sectorsSlice);
    queryUrl.searchParams.set("dateSelected", selectedDateSlice);
    queryUrl.searchParams.set("offset", stockReportList.length.toString());
    console.log(queryUrl.href);

    try {
      const response = await fetch(queryUrl.href);
      if (!response.ok) {
        // Check for an HTTP error status (e.g., 404 Not Found, 500 Internal Server Error)
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
      const newReports: StockReport[] = await response.json();
      if (newReports.length == 0) setHasMore(false);
      else {
        setStockReportList((oldReports) => [...oldReports, ...newReports]);
      }
      return;
    } catch (error) {
      console.log(error);
      setHasMore(false); //should I keep this line here?
    }
  }

  return (
    <>
      <Space direction={"vertical"} style={{ "--gap-vertical": "8px" }}>
        <h6>{"Report Type: " + reportTypeSlice}</h6>
        <h6>Click the picture to see news articles</h6>
       
        <List>
          {stockReportList.map((stockReport, index) => (
            <List.Item
              key={index}
              clickable={false}
              children={
                <>
                  <div style={{ maxWidth: "100%", fontSize : "105%"}}>
                    <div style={{fontWeight: "bold"}}>
                      {stockReport.stock.name}
                      <Divider direction="vertical" contentPosition="left" />
                      {stockReport.stock.exchange}
                      <Divider direction="vertical" contentPosition="left" />
                      {"Sector: " + stockReport.stock.sector}
                    </div>
                    <img
                      style={{ maxWidth: "100%", height: "auto" }}
                      src={`data:image/png;base64,${stockReport.reportImage}`}
                      onClick={() => {
                        Modal.info({
                          title: "News Articles",
                          content: (
                            <>
                              {stockReport.newsArticlesList.map(
                                (newsArticle, index) => (
                                  <>
                                    <div style={{ fontWeight: "bold" }}>
                                      {newsArticle.title}
                                    </div>
                                    {console.log(newsArticle.date)}
                                    <div>{newsArticle.date}</div>
                                    <div>{newsArticle.text}</div>
                                    <a
                                      href={newsArticle.url.toString()}
                                      target="_blank"
                                      rel="noreferrer"
                                    >
                                      Link
                                    </a>
                                    <p></p>
                                  </>
                                )
                              )}
                            </>
                          ),
                        });
                      }}
                    />
                    <div
                      style={{
                        marginTop: "10px",
                        display: "flex",
                        justifyContent: "space-between",
                      }}
                    >
                      <button
                        style={{ flex: "1", padding: "10px 20px" }}
                        onClick={() => {
                          window.open(
                            "https://stockcharts.com/h-sc/ui?s=" +
                              stockReport.stock.symbol,
                            "_blank"
                          );
                        }}
                      >
                        Link To StockCharts.com
                      </button>
                      <button
                        style={{ flex: "1", padding: "10px 20px" }}
                        onClick={() => {
                          window.open(
                            "https://www.google.com/search?q=" +
                              stockReport.stock.name,
                            "_blank"
                          );
                        }}
                      >
                        Link To Google Search
                      </button>
                    </div>
                  </div>
                </>
              }
            ></List.Item>
          ))}
        </List>
        <InfiniteScroll loadMore={retrieveMoreStockReports} hasMore={hasMore} />
      </Space>
    </>
  );
};
export default Reports;
