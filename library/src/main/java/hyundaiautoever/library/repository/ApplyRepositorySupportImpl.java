package hyundaiautoever.library.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hyundaiautoever.library.model.dto.ApplyDto.SearchApplyDto;
import hyundaiautoever.library.model.dto.QApplyDto_SearchApplyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static hyundaiautoever.library.model.entity.QApply.apply;

public class ApplyRepositorySupportImpl implements ApplyRepositorySupport{
    private final JPAQueryFactory queryFactory;

    public ApplyRepositorySupportImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<SearchApplyDto> searchApplyPage(Pageable pageable, String personalId, String name, String title, String author, String isbn, String publisher) {

        List<SearchApplyDto> content = queryFactory
                .select(new QApplyDto_SearchApplyDto(apply))
                .from(apply)
                .where(personalIdEq(personalId),
                        titleEq(title),
                        authorEq(author),
                        isbnEq(isbn),
                        publisherEq(publisher),
                        nameEq(name))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> countQuery = queryFactory
                .select(apply.count())
                .from(apply)
                .where(personalIdEq(personalId),
                        titleEq(title),
                        authorEq(author),
                        isbnEq(isbn),
                        publisherEq(publisher),
                        nameEq(name));
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression personalIdEq(String personalId) {
        return personalId == null ? null : apply.user.personalId.contains(personalId);
    }

    private BooleanExpression titleEq(String title) {
        return title == null ? null : apply.title.contains(title);
    }

    private BooleanExpression authorEq(String author) {
        return author == null ? null : apply.author.contains(author);
    }

    private BooleanExpression isbnEq(String isbn) {
        return isbn == null ? null : apply.isbn.contains(isbn);
    }

    private BooleanExpression publisherEq(String publisher) {
        return publisher == null ? null : apply.publisher.contains(publisher);
    }

    private BooleanExpression nameEq(String name) {
        return name == null ? null : apply.user.name.contains(name);
    }

}
