package az.ingress.mapstruct.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ExceptionResponse {
    private String title;
    private String status;
    private Map<String, String> validationErrors;

}
