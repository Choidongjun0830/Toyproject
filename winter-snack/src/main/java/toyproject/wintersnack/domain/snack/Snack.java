package toyproject.wintersnack.domain.snack;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Snack {

    private Long id;

    @NotEmpty
    private String name;
    @NotEmpty
    private String address;
    @NotEmpty
    @Min(500) @Max(100000)
    private Integer price;
    private String explanation;
    private String payMethod;
}
