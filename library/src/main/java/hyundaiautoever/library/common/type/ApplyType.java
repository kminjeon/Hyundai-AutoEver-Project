package hyundaiautoever.library.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApplyType {

    DELETE("DELETE"), COMPLETE("COMPLETE");
    private final String value;
}
