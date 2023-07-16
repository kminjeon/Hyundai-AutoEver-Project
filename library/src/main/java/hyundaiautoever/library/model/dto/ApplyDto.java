package hyundaiautoever.library.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import hyundaiautoever.library.model.dto.response.Response;
import hyundaiautoever.library.model.entity.Apply;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class ApplyDto {

    @Getter
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class UpdateApplyDto {
        private final String title;
        private final String author;
        private final String publisher;
        private final String isbn;

        public UpdateApplyDto(Apply apply) {
            this.title = apply.getTitle();
            this.author = apply.getAuthor();
            this.publisher = apply.getPublisher();
            this.isbn = apply.getIsbn();
        }
    }

    @Getter
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class GetApplyDto {
        private final Long applyId;
        private final String title;
        private final String author;
        private final String publisher;
        private final String applyDate;

        public GetApplyDto(Apply apply) {
            this.applyId = apply.getId();
            this.title = apply.getTitle();
            this.author = apply.getAuthor();
            this.publisher = apply.getPublisher();
            this.applyDate = apply.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        }
    }

    @Getter
    public static class GetApplyPage {
        private final Response.Pagination pagination;
        private final List<GetApplyDto> applyList;

        public GetApplyPage(Page<GetApplyDto> page) {
            this.pagination = new Response.Pagination(page);
            this.applyList = page.getContent();
        }
    }


    @Getter
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class SearchApplyDto {

        private final Long applyId;
        private final String personalId;
        private final String name;
        private final String title;
        private final String author;
        private final String publisher;
        private final String isbn;
        private final String applyDate;

        @QueryProjection
        public SearchApplyDto(Apply apply) {
            this.applyId = apply.getId();
            this.personalId = apply.getUser().getPersonalId();
            this.name = apply.getUser().getName();
            this.title = apply.getTitle();
            this.author = apply.getAuthor();
            this.publisher = apply.getPublisher();
            this.isbn = apply.getIsbn();
            this.applyDate = apply.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        }

    }

    @Getter
    public static class ApplyListPage {
        private final Response.Pagination pagination;
        private final List<SearchApplyDto> applyList;

        public ApplyListPage(Page<SearchApplyDto> page) {
            this.pagination = new Response.Pagination(page);
            this.applyList = page.getContent();
        }
    }

    public static UpdateApplyDto buildUpdateApplyDto(Apply apply) {
        return new UpdateApplyDto(apply);
    }
    public static ApplyListPage buildApplyListPage(Page<SearchApplyDto> page) {
        return new ApplyListPage(page);
    }

    public static GetApplyPage buildGetApplyPage(Page<GetApplyDto> page) {
        return new GetApplyPage(page);
    }

}
