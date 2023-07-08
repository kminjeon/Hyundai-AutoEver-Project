package hyundaiautoever.library.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hyundaiautoever.library.common.type.CategoryType;
import hyundaiautoever.library.model.dto.BookDto;
import hyundaiautoever.library.model.dto.BookDto.SearchAdminBookDto;
import hyundaiautoever.library.model.dto.QBookDto_SearchAdminBookDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

import static hyundaiautoever.library.model.entity.QApply.apply;
import static hyundaiautoever.library.model.entity.QBook.book;

public class BookRepositorySupportImpl implements BookRepositorySupport{
    private final JPAQueryFactory queryFactory;

    public BookRepositorySupportImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<SearchAdminBookDto> searchAdminBookPage(Pageable pageable, Long bookId, CategoryType categorytype, String title) {
        List<SearchAdminBookDto> content =  queryFactory
                .select(new QBookDto_SearchAdminBookDto(book))
                .from(book)
                .where(bookIdEq(bookId),
                        categoryTypeEq(categorytype),
                        titleEq(title))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        JPAQuery<Long> countQuery = queryFactory
                .select(book.count())
                .from(book)
                .where(bookIdEq(bookId),
                        categoryTypeEq(categorytype),
                        titleEq(title));
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression bookIdEq(Long bookId) {
        return bookId == null ? null : book.id.stringValue().contains(bookId.toString());
    }

    private BooleanExpression categoryTypeEq(CategoryType categoryType) {
        return categoryType == null ? null : book.categoryType.stringValue().contains(categoryType.toString());
    }

    private BooleanExpression titleEq(String title) {
        return title == null ? null : apply.title.contains(title);
    }

}
