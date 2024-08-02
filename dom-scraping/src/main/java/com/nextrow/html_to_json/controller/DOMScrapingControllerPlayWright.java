package com.nextrow.html_to_json.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.nextrow.html_to_json.service.DOMScrapingServicePlayWright;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DOMScrapingControllerPlayWright {

    @Autowired
    private DOMScrapingServicePlayWright domScrapingServicePlayWright;

    @GetMapping("/quip/v2/domScrappingUsingPlayWright")
    public ResponseEntity<ObjectNode> domScrappingPlayWright(@RequestParam String url) throws IOException, URISyntaxException {

        ObjectMapper objectMapper=new ObjectMapper();

        UrlValidator urlValidator = new UrlValidator();

        if(!urlValidator.isValid(url)){
            Map<String, String> response = new HashMap<>();
            response.put("status", "failed");
            response.put("errorCode",""+ HttpStatus.BAD_REQUEST.value());
            response.put("errorMessage", "Given url is not valid.");

            return new ResponseEntity<>(objectMapper.convertValue(response, ObjectNode.class), HttpStatus.BAD_REQUEST);
        }

        try{
            Playwright playwright = Playwright.create();
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));

            Page page = browser.newPage();
            return new ResponseEntity<>(objectMapper.convertValue(domScrapingServicePlayWright.domScrapping(url,page), ObjectNode.class), HttpStatus.OK);
        }
        catch (Exception exception) {
            Map<String, String> response = new HashMap<>();
            response.put("status", "failed");
            response.put("errorCode",""+ HttpStatus.BAD_REQUEST.value());
            response.put("errorMessage", "No webpage found with the given URL.");
            return new ResponseEntity<>(objectMapper.convertValue(response, ObjectNode.class), HttpStatus.BAD_REQUEST);
        }
    }
}