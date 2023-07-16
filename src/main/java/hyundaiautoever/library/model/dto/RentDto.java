package hyundaiautoever.library.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import hyundaiautoever.library.model.dto.response.Response;
import hyundaiautoever.library.model.entity.Rent;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class RentDto {

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class GetRentDto {
        private final Long rentId;
        private final String title;
        private final String author;
        private final String rentDate;
        private final String expectedReturnDate;
        private final Long lateDays;
        private final String isbn;
        private final Long bookId;
        public GetRentDto(Rent rent) {
            this.bookId = rent.getBook().getId();
            this.isbn = rent.getBook().getIsbn();
            this.rentId = rent.getId();
            this.title = rent.getBook().getTitle();
            this.author = rent.getBook().getAuthor();
            this.rentDate = rent.getRentDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            this.expectedReturnDate = rent.getExpectedReturnDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            this.lateDays = ChronoUnit.DAYS.between(LocalDate.now(), rent.getExpectedReturnDate());
        }
    }

    @Getter
    public static class GetRentPage {
        private final Response.Pagination pagination;
        private final List<GetRentDto> rentList;

        public GetRentPage(Page<GetRentDto> page) {
            this.pagination = new Response.Pagination(page);
            this.rentList = page.getContent();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class GetRentHistoryDto {
        private final Long rentId;
        private final String title;
        private final String author;
        private final String publisher;
        private final String rentDate;
        private final String returnDate;
        private final String isbn;
        private final Long bookId;

        public GetRentHistoryDto(Rent rent) {
            this.isbn = rent.getBook().getIsbn();
            this.bookId = rent.getBook().getId();
            this.rentId = rent.getId();
            this.title = rent.getBook().getTitle();
            this.author = rent.getBook().getAuthor();
            this.publisher = rent.getBook().getPublisher();
            this.rentDate = rent.getRentDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            this.returnDate = rent.getReturnDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        }
    }

    @Getter
    public static class GetRentHistoryPage {
        private final Response.Pagination pagination;
        private final List<GetRentHistoryDto> historyList;

        public GetRentHistoryPage(Page<GetRentHistoryDto> page) {
            this.pagination = new Response.Pagination(page);
            this.historyList = page.getContent();
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class GetAdminRentDto {
        private final Long rentId;
        private final String personalId;
        private final String name;
        private final Long bookId;
        private final String title;

        private final String author;
        private final String isbn;
        private final String rentDate;
        private final String expectedReturnDate;
        private final Long lateDays;

        @QueryProjection
        public GetAdminRentDto(Rent rent) {
            this.rentId = rent.getId();
            this.personalId = rent.getUser().getPersonalId();
            this.isbn = rent.getBook().getIsbn();
            this.name = rent.getUser().getName();
            this.author = rent.getBook().getAuthor();
            this.bookId = rent.getBook().getId();
            this.title = rent.getBook().getTitle();
            this.rentDate = rent.getRentDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            this.expectedReturnDate = rent.getExpectedReturnDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            this.lateDays = ChronoUnit.DAYS.between(LocalDate.now(), rent.getExpectedReturnDate());
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class GetAdminRentHistoryDto {
        private final Long rentId;
        private final String personalId;
        private final String name;
        private final Long bookId;
        private final String title;

        private final String author;
        private final String isbn;
        private final String rentDate;
        private final String returnDate;

        @QueryProjection
        public GetAdminRentHistoryDto(Rent rent) {
            this.rentId = rent.getId();
            this.personalId = rent.getUser().getPersonalId();
            this.isbn = rent.getBook().getIsbn();
            this.name = rent.getUser().getName();
            this.author = rent.getBook().getAuthor();
            this.bookId = rent.getBook().getId();
            this.title = rent.getBook().getTitle();
            this.rentDate = rent.getRentDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            this.returnDate = rent.getReturnDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        }
    }

    @Getter
    public static class GetAdminRentPage {
        private final Response.Pagination pagination;
        private final List<GetAdminRentDto> rentList;

        public GetAdminRentPage(Page<GetAdminRentDto> page) {
            this.pagination = new Response.Pagination(page);
            this.rentList = page.getContent();
        }
    }

    @Getter
    public static class GetAdminRentHistoryPage {
        private final Response.Pagination pagination;
        private final List<GetAdminRentHistoryDto> rentList;

        public GetAdminRentHistoryPage(Page<GetAdminRentHistoryDto> page) {
            this.pagination = new Response.Pagination(page);
            this.rentList = page.getContent();
        }
    }

    public static GetRentPage buildGetRentPage(Page<GetRentDto> page) {
        return new GetRentPage(page);
    }

    public static GetAdminRentPage buildGetAdminRentPage(Page<GetAdminRentDto> page) {
        return new GetAdminRentPage(page);
    }

    public static GetAdminRentHistoryPage buildGetAdminRentHistoryPage(Page<GetAdminRentHistoryDto> page) {
        return new GetAdminRentHistoryPage(page);
    }

    public static GetRentHistoryPage buildGetRentHistoryPage(Page<GetRentHistoryDto> page) {
        return new GetRentHistoryPage(page);
    }


}
