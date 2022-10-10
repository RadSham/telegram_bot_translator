package com.example.telegbot.controller;

import com.example.telegbot.service.WordService;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@AllArgsConstructor
public class WordController {

    @Autowired
    WordService wordService;

    @RequestMapping(path = "/getword", method = RequestMethod.GET)
    public String getWord(String word1, String langCode) throws UnirestException {
        return wordService.translate(word1, langCode);
    }

}
