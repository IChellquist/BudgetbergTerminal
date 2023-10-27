package IChellquist.BudgetbergTerminal.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Stock {

    private String name;
    private String symbol;
    private String exchange;
    private String sector;
    private String industry;
}
