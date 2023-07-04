package hyundaiautoever.library.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthType {
    ADMIN("ADMIN"), USER("USER");
    private final String value;
}
