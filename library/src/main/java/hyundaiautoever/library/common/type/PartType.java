package hyundaiautoever.library.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PartType {
    MOBIS("MOBIS"), PLATFORM("PLATFORM"), FACTORY("FACTORY"), NAVIGATION("NAVIGATION");
    private final String value;
}
