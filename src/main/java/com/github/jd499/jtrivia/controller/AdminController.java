package com.github.jd499.jtrivia.controller;

import com.github.jd499.jtrivia.service.ScraperService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ScraperService scraperService;

    @PostMapping("/scrape/seasons")
    public String scrapeSeasons(@RequestParam int startSeason, @RequestParam int endSeason) {
        scraperService.scrapeSeasons(startSeason, endSeason);
        return "Scraping initiated for seasons " + startSeason + " to " + endSeason;
    }
}
