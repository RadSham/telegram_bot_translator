package com.example.telegbot.service;

import com.example.telegbot.model.Word;
import com.example.telegbot.repository.WordRepository;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;


@Service
@AllArgsConstructor
@NoArgsConstructor
public class WordService {

    @Autowired
    WordRepository wordRepository;

    /**
     * Translate source text.
     *
     * @return translated text.
     */
    public String translate(String word1, String langCode) throws UnirestException {
        StringBuilder stringBuilder = new StringBuilder();
        String s = word1.toLowerCase(Locale.ROOT);
        if (langCode.equals("LanguageOne - LanguageTwo")) {
            if (wordRepository.getAllByName(s).isEmpty()) return "Can't translate a word: " + s;
            List<Word> wordList = wordRepository.getAllByName(s);
            for (Word w : wordList) {
                stringBuilder.append(w.getNameTwo()).append("\n");
            }
        } else if (langCode.equals("LanguageTwo - LanguageOne")) {
            if (wordRepository.getAllByNameTwo(s).isEmpty()) return "Can't translate a word: " + s;
            List<Word> wordList = wordRepository.getAllByNameTwo(s);
            for (Word w : wordList) {
                stringBuilder.append(w.getName()).append("\n");
            }
        }
        return stringBuilder.toString();
    }

}
