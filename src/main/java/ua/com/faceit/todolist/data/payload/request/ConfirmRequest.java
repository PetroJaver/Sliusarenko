package ua.com.faceit.todolist.data.payload.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmRequest {

    @NotBlank
    private String confirmToken;
}
