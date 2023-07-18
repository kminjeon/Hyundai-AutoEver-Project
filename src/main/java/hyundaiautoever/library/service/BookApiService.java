package hyundaiautoever.library.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hyundaiautoever.library.common.exception.ExceptionCode;
import hyundaiautoever.library.common.exception.LibraryException;
import hyundaiautoever.library.common.type.CategoryType;
import hyundaiautoever.library.model.dto.BookResult;
import hyundaiautoever.library.model.dto.JsonResult;
import hyundaiautoever.library.model.dto.request.BookRequest;
import hyundaiautoever.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true) // 조회 성능 최적화
public class BookApiService {

    @Autowired
    BookService bookService;

    @Autowired
    ImageService imageService;

    @Autowired
    BookRepository bookRepository;

    @Value("${naver.clientId}")
    private String naverId;

    @Value("${naver.clientSecret}")
    private String naverSecret;

    public String getJson(String query, int display, CategoryType categoryType) {
        if (bookRepository.existsByIsbn(query)) {
            throw new LibraryException.AlreadyExistBook(ExceptionCode.ALREADY_EXIST_ERROR);
        }
        String clientId = naverId;
        String clientSecret = naverSecret;

        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/search/book.json")
                .queryParam("query", query)
                .queryParam("display", display)
                .queryParam("start", 1)
                .encode()
                .build()
                .toUri();

        RequestEntity<Void> req = RequestEntity
                .get(uri)
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", clientSecret)
                .build();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = restTemplate.exchange(req, String.class);

        ObjectMapper om = new ObjectMapper();
        JsonResult jsonResult = null;

        try {
            jsonResult = om.readValue(resp.getBody(), JsonResult.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (jsonResult.getTotal() == 0) {
            throw new LibraryException.EmptyResultBook(ExceptionCode.EMPTY_RESULT_BOOK_ERROR);
        }

        BookResult firstBook = jsonResult.getItems().get(0);

        if (bookRepository.existsByIsbn(firstBook.getIsbn())) {
            throw new LibraryException.AlreadyExistBook(ExceptionCode.ALREADY_EXIST_ERROR);
        }
        BookRequest.AddBookRequest request = new BookRequest.AddBookRequest();
        request.setTitle(firstBook.getTitle());
        request.setAuthor(firstBook.getAuthor());
        request.setIsbn(firstBook.getIsbn());
        request.setPublisher(firstBook.getPublisher());
        request.setCategoryType(categoryType);
        request.setDescription(firstBook.getDescription());
        bookService.addBook(request);

        // 이미지 저장
        imageService.callImage(firstBook.getImage(), firstBook.getIsbn());

        return firstBook.getTitle();
    }
}
