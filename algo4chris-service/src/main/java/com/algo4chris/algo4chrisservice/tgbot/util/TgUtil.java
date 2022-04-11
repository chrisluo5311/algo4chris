package com.algo4chris.algo4chrisservice.tgbot.util;

import com.algo4chris.algo4chriscommon.common.response.MgrResponseDto;
import com.algo4chris.algo4chrisdal.models.Member;
import com.algo4chris.algo4chrisservice.tgbot.enums.TelegramInfo;
import com.algo4chris.algo4chrisdal.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Optional;

/**
 * tg service層
 *
 * @author chris
 * */
@Slf4j
@Service
public class TgUtil {

    @Resource
    UserRepository userRepository;

    /**
     * 接收tg指令並做出相對應的動作
     *
     * @param message 訊息
     * */
    public void commandInstruct(String message){
        switch (message){
            case "查詢用戶資訊":
                List<Member> memberList = userRepository.findAll();
                sendMessage(MgrResponseDto.success(memberList));
                break;
            default:
                sendMessage(message);
        }
    }

    public void sendMessage(MgrResponseDto mgrResponseDto){
        sendMessage(mgrResponseDto.toString());
    }

    public void sendMessage(String message){
        sendMessage(TelegramInfo.JWT_DEMO_ERROR_BOT,null,message);
    }

    public static void sendMessage(TelegramInfo telegramInfo, String title, String message) {
        try {
            String TG_POST_URL = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
            String botToken = telegramInfo.getToken();
            String chatId = telegramInfo.getChatId();
            String text = URLEncoder.encode(Optional.ofNullable(title).orElse("") + message, "UTF-8");

            TG_POST_URL = String.format(TG_POST_URL, botToken, chatId, text);

            URL url = new URL(TG_POST_URL);

            URLConnection conn = url.openConnection();

            StringBuilder sb = new StringBuilder();
            InputStream is = new BufferedInputStream(conn.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            String response = sb.toString();
            log.info(telegramInfo.getName() +",發送訊息: "+ message);
        } catch (Exception e) {
            log.error(telegramInfo.getName() + " fail");
            log.error(e.getMessage(), e);
        }
    }

//    public static void main(String[] args) throws UnsupportedEncodingException {
//        TgUtil.sendMessage(TelegramInfo.JWT_DEMO_ERROR_BOT,"测试","大兽打吉");
//    }

}
