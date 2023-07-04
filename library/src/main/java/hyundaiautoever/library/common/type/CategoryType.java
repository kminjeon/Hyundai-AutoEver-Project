package hyundaiautoever.library.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CategoryType {
    DEV("DEV"), NOVEL("NOVEL"), SCIENCE("SCIENCE"), ECONOMY("ECONOMY"), HUMANITY("HUMANITY");
    private final String value;
}
