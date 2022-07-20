package com.shopme.admin.telegram;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class TelegramBots extends TelegramLongPollingBot {

	@Override
	public void onUpdateReceived(Update update) {
		// TODO Auto-generated method stub
		
		 System.out.println("Message chat word!!");
         String message_text = update.getMessage().getText();
         long chat_id = update.getMessage().getChatId();

         SendMessage message = new SendMessage();
        message.setChatId(chat_id);
        message.setText(message_text);

         try {
             execute(message);
         } catch (TelegramApiException ex) {
           //  ex.printStackTrace();
         }
		
	}

	@Override
	public String getBotUsername() {
		// TODO Auto-generated method stub
		return "@ibtesting_bot";
	}

	@Override
	public String getBotToken() {
		// TODO Auto-generated method stub
		return "5566568060:AAE6CzVDsxGBIZqKwaEBGlPHAzh4saur1Lw";
	}

}
