package com.github.jd499.jtrivia.scraper;

import com.github.jd499.jtrivia.model.Category;
import com.github.jd499.jtrivia.model.Clue;
import com.github.jd499.jtrivia.model.RoundType;
import com.github.jd499.jtrivia.repository.CategoryRepository;
import com.github.jd499.jtrivia.repository.ClueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@Component
@RequiredArgsConstructor
public class JArchiveScraper {
    private static final String BASE_URL = "http://j-archive.com";
    private final CategoryRepository categoryRepository;
    private final ClueRepository clueRepository;

    private record RoundInfo(String selector, RoundType roundType, boolean doubleValue) {
        public int adjustValue(int value) {
            return doubleValue ? value * 2 : value;
        }
    }

    private static final RoundInfo[] ROUNDS = {
            new RoundInfo("#jeopardy_round", RoundType.SINGLE_JEOPARDY, false),
            new RoundInfo("#double_jeopardy_round", RoundType.DOUBLE_JEOPARDY, true)
    };

    @Transactional
    public void scrapeSeasons(int startSeason, int endSeason) {
        log.info("Starting scrape for seasons {} to {}", startSeason, endSeason);

        List<String> gameIds = new ArrayList<>();
        for (int i = startSeason; i <= endSeason; i++) {
            try {
                String seasonsUrl = BASE_URL + "/showseason.php?season=" + i;
                log.info("Fetching season URL: {}", seasonsUrl);

                Document seasonList = Jsoup.connect(seasonsUrl)
                        .userAgent("Mozilla/5.0")
                        .get();

                Elements linkList = seasonList.select("table td a");
                log.info("Found {} game links in season {}", linkList.size(), i);

                for (Element ll : linkList) {
                    String href = ll.attr("href");
                    String[] hrefParts = href.split("id=");
                    if (hrefParts.length > 1) {
                        gameIds.add(hrefParts[1]);
                        log.debug("Added game ID: {}", hrefParts[1]);
                    }
                }
            } catch (IOException e) {
                log.error("Error getting season {}: {}", i, e.getMessage());
            }
        }

        log.info("Total game IDs collected: {}", gameIds.size());

        for (String gid : gameIds) {
            try {
                scrapeGame(gid);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                log.error("Error processing game {}: {}", gid, e.getMessage(), e);
            }
        }
    }

    @Transactional
    protected void scrapeGame(String gameId) throws IOException {
        log.info("Starting to scrape game ID: {}", gameId);

        String gameurl = "http://www.j-archive.com/showgame.php?game_id=" + gameId;
        Document game = Jsoup.connect(gameurl)
                .userAgent("Mozilla/5.0")
                .get();


        LocalDateTime airdate = parseAirDate(game);


        for (RoundInfo round : ROUNDS) {
            scrapeRegularRound(game, round, airdate, Integer.parseInt(gameId));
        }


        scrapeFinalJeopardy(game, airdate, Integer.parseInt(gameId));
    }

    private void scrapeRegularRound(Document game, RoundInfo round, LocalDateTime airdate, Integer gameId) {

        Elements categoryElements = game.select(round.selector + " .category .category_name");
        List<Category> categories = new ArrayList<>();
        log.info("Found {} categories in {}", categoryElements.size(), round.selector);

        for (Element categoryElement : categoryElements) {
            String categoryName = categoryElement.text().toLowerCase();
            Category category = findOrCreateCategory(categoryName);
            categories.add(category);
        }


        Elements clueElements = game.select(round.selector + " .clue");
        log.info("Found {} clues in {}", clueElements.size(), round.selector);

        for (Element clueElement : clueElements) {
            try {
                Element questionElement = clueElement.selectFirst(".clue_text");
                Element answerElement = clueElement.selectFirst("em.correct_response");

                if (questionElement == null || answerElement == null) continue;

                String question = questionElement.text().trim();
                String answer = answerElement.text().trim();


                Element valueElement = clueElement.selectFirst(".clue_value, .clue_value_daily_double");
                Integer value = null;
                if (valueElement != null) {
                    String valueText = valueElement.text().replaceAll("[^0-9]", "");
                    value = round.adjustValue(Integer.parseInt(valueText));
                }


                Element categoryCell = clueElement.parent();
                if (categoryCell != null) {
                    int categoryIndex = categoryCell.elementSiblingIndex();
                    if (categoryIndex >= 0 && categoryIndex < categories.size()) {
                        createClue(question, answer, categories.get(categoryIndex),
                                value, airdate, gameId, round.roundType);
                    }
                }
            } catch (Exception e) {
                log.error("Error processing clue in {}: {}", round.roundType, e.getMessage());
            }
        }
    }

    private void scrapeFinalJeopardy(Document game, LocalDateTime airdate, Integer gameId) {
        try {
            Element categoryElement = game.selectFirst("#final_jeopardy_round .category_name");
            Element clueElement = game.selectFirst("#final_jeopardy_round .clue_text");
            Element answerElement = game.selectFirst("#final_jeopardy_round em.correct_response");

            if (categoryElement != null && clueElement != null && answerElement != null) {
                String categoryName = categoryElement.text().toLowerCase();
                String question = clueElement.text().trim();
                String answer = answerElement.text().trim();

                Category category = findOrCreateCategory(categoryName);
                createClue(question, answer, category, null, airdate, gameId, RoundType.FINAL_JEOPARDY);
            }
        } catch (Exception e) {
            log.error("Error processing Final Jeopardy: {}", e.getMessage());
        }
    }

    private LocalDateTime parseAirDate(Document game) {
        Element titleElement = game.selectFirst("#game_title h1");
        if (titleElement != null) {
            String dateText = titleElement.text().replaceAll("Show #\\d+ - ", "").trim();
            dateText = dateText.replaceAll("^\\w+day, ", "");
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH);
                return LocalDateTime.parse(dateText + " 00:00",
                        DateTimeFormatter.ofPattern("MMMM d, yyyy HH:mm", Locale.ENGLISH));
            } catch (Exception e) {
                log.error("Error parsing date {}: {}", dateText, e.getMessage());
            }
        }
        return null;
    }

    @Transactional
    protected Category findOrCreateCategory(String title) {
        return categoryRepository.findByTitle(title)
                .orElseGet(() -> {
                    Category category = new Category();
                    category.setTitle(title);
                    category.setCluesCount(0);
                    return categoryRepository.save(category);
                });
    }

    @Transactional
    protected void createClue(String question, String answer, Category category,
                              Integer value, LocalDateTime airdate, Integer gameId,
                              RoundType roundType) {
        try {
            Clue clue = new Clue();
            clue.setQuestion(question);
            clue.setAnswer(answer);
            clue.setCategory(category);
            clue.setValue(value);
            clue.setAirdate(airdate);
            clue.setGameId(gameId);
            clue.setInvalidCount(0);
            clue.setRoundType(roundType);

            category.setCluesCount(category.getCluesCount() + 1);
            categoryRepository.save(category);

            clueRepository.save(clue);
            log.debug("Saved {} clue for category: {}", roundType, category.getTitle());
        } catch (Exception e) {
            log.error("Error saving {} clue: {}", roundType, e.getMessage());
        }
    }
}