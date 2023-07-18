package hyundaiautoever.library.controller;

import hyundaiautoever.library.common.type.CategoryType;
import hyundaiautoever.library.model.dto.request.BookRequest;
import hyundaiautoever.library.model.dto.response.Response;
import hyundaiautoever.library.service.BookApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@Slf4j
public class BookApiController {

    @Autowired
    BookApiService bookApiService;

    @PostMapping("/getJson")
    public Response getJson(@RequestParam("query") String query,
                            @RequestParam("display") int display,
                            @RequestParam("categoryType") CategoryType categoryType) {
        log.info("BookAPIController : [getJson]");
        bookApiService.getJson(query, display, categoryType);
        return Response.ok();
    }

}
