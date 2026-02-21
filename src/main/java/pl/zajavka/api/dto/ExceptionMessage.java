package pl.zajavka.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.context.annotation.Bean;

@Value
@Builder
@AllArgsConstructor(staticName = "of")
public class ExceptionMessage {

    String errorId;

}
