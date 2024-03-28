package com.spring.auth.client.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.auth.domain.pass.PassResult;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserInfoFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;
    private final String PATH= "/pass/user";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ServletException {
        String requestURI = request.getRequestURI();
        if(requestURI.contains(PATH)) {
            String requestData = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
            log.info("requestData = {}" , requestData);
            try {
                String jsonData = decodeTwice(requestData);
                String jsonString = jsonData.substring(jsonData.indexOf("{"), jsonData.lastIndexOf("}") + 1);
                PassResult result = objectMapper.readValue(jsonString, PassResult.class);

                request.setAttribute("result", result);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        filterChain.doFilter(request, response);
    }

    public static String decodeTwice(String encodedData) {
        String decodedOnce = URLDecoder.decode(encodedData, StandardCharsets.UTF_8);
        String decodedTwice = URLDecoder.decode(decodedOnce, StandardCharsets.UTF_8);
        return decodedTwice;
    }

}
