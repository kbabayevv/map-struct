package az.ingress.mapstruct.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserRequest {
    @NotBlank(message = "Name can not be empty")
    String name;
    @NotBlank(message = "Surname can not be empty")
    String surname;

    @NotNull
    @Min(value = 10)
    Integer age;
}
