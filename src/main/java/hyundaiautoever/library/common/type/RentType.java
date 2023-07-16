package hyundaiautoever.library.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RentType {

    OPEN("OPEN"), CLOSE("CLOSE");
    private final String value;
}
