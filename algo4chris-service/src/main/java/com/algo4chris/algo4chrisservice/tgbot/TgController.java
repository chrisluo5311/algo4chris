package com.algo4chris.algo4chrisservice.tgbot;

import com.algo4chris.algo4chrisservice.tgbot.request.TelegramMessage;
import com.algo4chris.algo4chrisservice.tgbot.util.TgUtil;
import com.algo4chris.algo4chrisdal.session.SessionEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/tg")
public class TgController {

    @Resource
    ObjectMapper objectMapper;

    @Resource
    TgUtil tgService;

    @PostMapping("/receive")
    public void receive(@RequestBody TelegramMessage telegramMessage, SessionEntity clientSessionDto) {
        log.info("接收tg讯习:{}",telegramMessage.toString());
        Map<String, Object> map = (Map<String, Object>) telegramMessage.getMessage();
        
        tgService.commandInstruct((String) map.get("text"));
    }


}
