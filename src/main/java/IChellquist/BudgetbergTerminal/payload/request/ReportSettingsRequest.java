package IChellquist.BudgetbergTerminal.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportSettingsRequest {

    @NotBlank
    private String reportType;

    @NotEmpty
    private List<String> exchanges;

    @NotEmpty
    private List<String> sectors;
    @Past
    private Date dateSelected;

}
