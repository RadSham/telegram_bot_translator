package com.example.telegbot;

import com.example.telegbot.service.WordService;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Component
public class TelegbotBot extends TelegramLongPollingBot {

    private final String botToken;
    private final String botUsername;
    private final WordService wordService;

    private final HashMap<Long, String> userCodes = new HashMap<>();

    public TelegbotBot(TelegramBotsApi telegramBotsApi,
                       @Value("${telegram.bot-token}") String botToken,
                       @Value("${telegram.bot-name}") String botUsername,
                       WordService wordService) throws TelegramApiException {
        this.botToken = botToken;
        this.botUsername = botUsername;
        this.wordService = wordService;
        telegramBotsApi.registerBot(this);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();
            long uid = update.getMessage().getFrom().getId();

            switch (message_text) {
                case "/start": {
                    String answer = "I am a dictionary bot. Select translation languages\n";
                    answer += "To reset the setting, send the command /revoke";
                    String markupString = "LanguageOne - LanguageTwo;LanguageTwo - LanguageOne";

                    SendMessage message = new SendMessage();
                    message.setChatId(chat_id);
                    message.setText(answer);
                    message.setReplyMarkup(createInlineKeyboardMarkup(markupString));

                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case "/revoke": {

                    userCodes.remove(uid);

                    String answer = "Your language has been reset. Now you need to reselect the language. ";
                    answer += "To do this, send the command /start";

                    SendMessage message = new SendMessage();
                    message.setChatId(chat_id);
                    message.setText(answer);

                    try {
                        execute(message);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                default:
                    if (userCodes.get(uid) != null) {
                        String langCode = userCodes.get(uid);
                        String answer = "I can not translate.";

                        try {
                            answer = wordService.translate(message_text, langCode);
                        } catch (UnirestException e) {
                            System.out.println(e.getMessage());
                        }

                        SendMessage message = new SendMessage();
                        message.setChatId(chat_id);
                        message.setText(answer);
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    } else {
                        String answer = "First select the language I need to translate into. ";
                        answer += "To do this, send the command /start";

                        SendMessage message = new SendMessage();
                        message.setChatId(chat_id);
                        message.setText(answer);
                        try {
                            execute(message);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        } else if (update.hasCallbackQuery()) {
            String langCode = update.getCallbackQuery().getData();
            long uid = update.getCallbackQuery().getFrom().getId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();

            userCodes.put(uid, langCode);

            String answer = "You chose *" + langCode + "*.\nI'm ready to translate.";

            SendMessage message = new SendMessage();
            message.setChatId(chat_id);
            message.setParseMode("markdown");
            message.setText(answer);
            try {
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Create inline markup from string.
     *
     * @param buttons - string type as "button1;button2;button3"
     * @return InlineKeyboardMarkup - ready inline markup object.
     */
    private InlineKeyboardMarkup createInlineKeyboardMarkup(String buttons) {

        String[] buttonsArray = buttons.split(";");

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline;
        rowsInline = new ArrayList<>();

        for (String button : buttonsArray) {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(button);
            inlineKeyboardButton.setCallbackData(button);
            rowInline.add(inlineKeyboardButton);
            rowsInline.add(rowInline);
        }
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

}


