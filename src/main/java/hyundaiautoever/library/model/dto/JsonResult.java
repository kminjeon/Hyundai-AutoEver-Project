package hyundaiautoever.library.model.dto;

import lombok.*;

import java.util.List;


@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class JsonResult {
    private String lastBuildDate;
    private int total;
    private int start;
    private int display;
    private List<BookResult> items;
}
