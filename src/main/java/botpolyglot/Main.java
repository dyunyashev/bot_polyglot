package botpolyglot;

import botpolyglot.translate.TranslationService;
import botpolyglot.translate.YandexTranslate;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        try {
            TranslationService ts = new YandexTranslate();
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new TranslateBot(ts));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
