package hyundaiautoever.library.service;


import hyundaiautoever.library.common.exception.ExceptionCode;
import hyundaiautoever.library.common.exception.LibraryException;
import hyundaiautoever.library.model.entity.User;
import hyundaiautoever.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true) // 조회 성능 최적화
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final UserService userService;

    private final UserRepository userRepository;

    public String sendMail(String personalId) {

        User user = userRepository.findByPersonalId(personalId).orElseThrow(() -> {
            log.error("sendMail Exception : [존재하지 않는 User ID]", ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
            return new LibraryException.DataNotFoundException(ExceptionCode.DATA_NOT_FOUND_EXCEPTION);
        });

        String authNum = createCode(); // 랜덤 코드 생성
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(user.getEmail()); // 메일 수신자
            mimeMessageHelper.setSubject("[HYUNDAI LIBRARY] 비밀번호 재설정을 위한 인증 코드 발송"); // 메일 제목
            mimeMessageHelper.setText(authNum);
            javaMailSender.send(mimeMessage);
            log.info("Success");
            return authNum;

        } catch (Exception e) {
            log.error("sendMail Exception : {}", e.getMessage());
            throw new LibraryException.FailSendEmailException(ExceptionCode.FAIL_SEND_EMAIL_ERROR);
        }
    }

    public void sendRentEmail(String email, String title) {

        log.info("EmailService : [sendRentEmail]");
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
            mimeMessageHelper.setTo(email); // 메일 수신자
            mimeMessageHelper.setSubject("[HYUNDAI LIBRARY] 예약하신 도서가 준비되었습니다."); // 메일 제목
            mimeMessageHelper.setText("\" " + title + " \""+ " 도서가 대여 완료되었습니다. 도서관에서 도서를 수령하시길 바랍니다.");
            javaMailSender.send(mimeMessage);
            log.info("Success");

        } catch (Exception e) {
            log.error("sendRentEmail Exception : {}", e.getMessage());
            throw new LibraryException.FailSendEmailException(ExceptionCode.FAIL_SEND_EMAIL_ERROR);
        }
    }

    // 인증번호 생성 메서드
    public String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(4);

            switch (index) {
                case 0: key.append((char) ((int) random.nextInt(26) + 97)); break;
                case 1: key.append((char) ((int) random.nextInt(26) + 65)); break;
                default: key.append(random.nextInt(9));
            }
        }
        return key.toString();
    }
}
