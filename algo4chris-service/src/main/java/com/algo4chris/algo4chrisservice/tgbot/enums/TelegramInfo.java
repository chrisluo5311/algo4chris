package com.algo4chris.algo4chrisservice.tgbot.enums;

/**
 * 可使用 https://api.telegram.org/bot5017095330:AAFEe0HlyiwhKyimgxwXw_ym_KUZiSa3KZo/getUpdates
 * 获取chatId: group为负数
 * chris: 1966832279
 *
 * @author chris
 * */
public enum TelegramInfo {
    JWT_DEMO_ERROR_BOT("5017095330:AAFEe0HlyiwhKyimgxwXw_ym_KUZiSa3KZo", "1966832279","chrishamburger"),
    JWT_DEMO_ERROR_BOT_FOR_JAVA_GROUP("5017095330:AAFEe0HlyiwhKyimgxwXw_ym_KUZiSa3KZo", "-574703697","chrishamburger");

    /** tg bot token */
    private String token;
    /** 要传送的目地的 */
    private String chatId;
    private String name;

    TelegramInfo(String token, String chatId, String name) {
        this.token = token;
        this.chatId = chatId;
        this.name = name;
    }
    public String getToken() {
        return this.token;
    }
    public String getChatId() {
        return this.chatId;
    }
    public String getName() {
        return this.name;
    }
}
