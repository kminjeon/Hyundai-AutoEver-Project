package hyundaiautoever.library.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true) // 조회 성능 최적화
public class ImageService {

    public void callImage(String imageUrl, String isbn) {
        String destinationPath = "src/main/reactfront/public/img/book/" + isbn + ".jpg";
        try {
            saveImage(imageUrl, destinationPath);
            System.out.println("이미지 다운로드가 완료되었습니다.");
        } catch (IOException e) {
            System.out.println("이미지 다운로드 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    public static void saveImage(String imageUrl, String destinationPath) throws IOException {
        URL url = new URL(imageUrl);
        BufferedInputStream inputStream = new BufferedInputStream(url.openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(destinationPath);

        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) != -1) {
            fileOutputStream.write(buffer, 0, bytesRead);
        }

        fileOutputStream.close();
        inputStream.close();
    }
}
