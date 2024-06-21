package com.nextrow.html_to_json.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nextrow.html_to_json.service.HtmlConverterService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.net.URISyntaxException;


@RestController
public class HtmlConverterController {

    @Autowired
    private HtmlConverterService htmlConverterService;


    @GetMapping("/quip/v2/webScraping")
    public ResponseEntity<ObjectNode> webScraping(@RequestParam String url) throws IOException, URISyntaxException {

        // to get data from a web url

        Document document = Jsoup.connect(url).get();
        ObjectMapper objectMapper = new ObjectMapper();

        return new ResponseEntity<>(objectMapper.convertValue(htmlConverterService.generateHtmlToJson(document, url),ObjectNode.class), HttpStatus.OK);
    }

}

