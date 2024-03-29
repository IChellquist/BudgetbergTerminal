package IChellquist.BudgetbergTerminal.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "roles")
@Getter
@Setter
@AllArgsConstructor
@Builder
public class Role {
    @Id
    private String id;
    private ERole name;
}
