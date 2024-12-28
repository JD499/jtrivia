package com.github.jd499.jtrivia.service;

import com.github.jd499.jtrivia.scraper.JArchiveScraper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScraperService {
    private final JArchiveScraper scraper;

    @Transactional
    public void scrapeSeasons(int startSeason, int endSeason) {
        scraper.scrapeSeasons(startSeason, endSeason);
    }
}