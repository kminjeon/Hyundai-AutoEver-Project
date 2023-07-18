package hyundaiautoever.library.model.dto;

import lombok.*;
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class BookResult {
    private String title;
    private String link;
    private String image;
    private String author;
    private String discount;
    private String publisher;
    private String pubdate;
    private String isbn;
    private String description;
}
