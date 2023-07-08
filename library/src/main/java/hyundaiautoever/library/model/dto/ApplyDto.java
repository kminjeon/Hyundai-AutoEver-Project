package hyundaiautoever.library.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import hyundaiautoever.library.model.dto.response.Response;
import hyundaiautoever.library.model.entity.Apply;
import lombok.Getter;
import org.springframework.data.domain.Page;

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
    public static class SearchApplyDto {
        private final String personalId;
        private final String title;
        private final String author;
        private final String publisher;
        private final String isbn;

        @QueryProjection
        public SearchApplyDto(Apply apply) {
            this.personalId = apply.getUser().getPersonalId();
            this.title = apply.getTitle();
            this.author = apply.getAuthor();
            this.publisher = apply.getPublisher();
            this.isbn = apply.getIsbn();
        }

    }

    @Getter
    public static class ApplyListPage {
        private final Response.Pagination pagination;
        private final List<SearchApplyDto> searchApplyDtoList;

        public ApplyListPage(Page<SearchApplyDto> page) {
            this.pagination = new Response.Pagination(page);
            this.searchApplyDtoList = page.getContent();
        }
    }

    public static UpdateApplyDto buildUpdateApplyDto(Apply apply) {
        return new UpdateApplyDto(apply);
    }
    public static ApplyListPage buildApplyListPage(Page<SearchApplyDto> page) {
        return new ApplyListPage(page);
    }
}
