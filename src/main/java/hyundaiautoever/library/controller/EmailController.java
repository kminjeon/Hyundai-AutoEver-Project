package hyundaiautoever.library.controller;

import hyundaiautoever.library.model.dto.response.Response;
import hyundaiautoever.library.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
public class EmailController {

    @Autowired
    private EmailService emailService;

    /**
     * 비밀번호 재설정을 위한 인증코드 이메일 발송
     * @param personalId
     * @return 인증코드
     */
    @PostMapping("/email")
    public Response sendJoinMail(@RequestParam String personalId) {
        return Response.ok().setData(emailService.sendMail(personalId));
    }
}
