package com.spring.auth.pass.service;

import com.spring.auth.domain.pass.PassResult;
import com.spring.auth.pass.config.PassProperties;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@RequiredArgsConstructor
@Slf4j
public class PassService {

    private final PassProperties passProperties;

    /**
     * 본인 인증 표준창 팝업
     * 이용기관 정보 거래요청 정보 생성
     * */
    public String getUserToken(HttpSession session){
        String clientPrefix = passProperties.getClientPrefix();

        //로직구현

        /* 1.6 거래 요청 정보 JSON 반환 */
        return "";
    }

    /**
     * 본인 인증 내용으로 개인 정보 수집
     * */
    public Account getUserInfo(PassResult result){

        //로직 구현


        return Account.builder()
                .name("userName")
                .mobile("userPhone")
                .ci("ci")
               // .di(di)
                .gender("userGender")
             //   .birthDate(userBirthday)
             //   .reqDate(reqDate)
                .build();
    }

    /**
     * 본인확인 서버 통신
     * */
    public String sendPost(String dest, String jsonData) {
        BufferedReader bufferedReader = null;
        HttpURLConnection connection = null;
        DataOutputStream dataOutputStream = null;
        try {
            URL url = new URL(dest);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setDoOutput(true);

            dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.write(jsonData.getBytes("UTF-8"));

            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer responseData = new StringBuffer();
            String info;
            while ((info = bufferedReader.readLine()) != null) {
                responseData.append(info);
            }
            return responseData.toString();
        } catch (FileNotFoundException e) {
           throw new RuntimeException(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }

                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }

                if (connection != null) {
                    connection.disconnect();
                }
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
