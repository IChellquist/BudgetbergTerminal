package IChellquist.BudgetbergTerminal.models;

import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class NewsArticle {
    private String title;
    private String url;
    private String text;
    private Date date;

}
