package hyundaiautoever.library.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import hyundaiautoever.library.model.dto.response.Response;
import hyundaiautoever.library.model.entity.Reserve;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReserveDto {
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class GetReserveDto {

        private final Long reserveId;
        private final String title;
        private final String author;
        private final String publisher;
        private final String reserveDate;
        private final Integer waitNumber;
        public GetReserveDto(Reserve reserve) {
            this.reserveId = reserve.getId();
            this.title = reserve.getBook().getTitle();
            this.author = reserve.getBook().getAuthor();
            this.publisher = reserve.getBook().getPublisher();
            this.reserveDate = reserve.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            this.waitNumber = reserve.getWaitNumber();
        }
    }
    @Getter
    public static class GetReservePage {
        private final Response.Pagination pagination;
        private final List<GetReserveDto> reserveList;

        public GetReservePage(Page<GetReserveDto> page) {
            this.pagination = new Response.Pagination(page);
            this.reserveList = page.getContent();
        }
    }

    public static GetReservePage buildReservePage(Page<GetReserveDto> page) {
        return new GetReservePage(page);
    }
}
