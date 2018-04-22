package com.daeliin.components.cms.news;

import com.daeliin.components.cms.credentials.account.Account;
import com.daeliin.components.cms.credentials.account.AccountService;
import com.daeliin.components.cms.event.EventLogService;
import com.daeliin.components.cms.sql.BNews;
import com.daeliin.components.cms.sql.QAccount;
import com.daeliin.components.cms.sql.QNews;
import com.daeliin.components.core.resource.Id;
import com.daeliin.components.core.string.UrlFriendlyString;
import com.daeliin.components.persistence.resource.service.ResourceService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

@Service
public class NewsService extends ResourceService<News, BNews, String, NewsRepository> {

    private final AccountService accountService;
    private final EventLogService eventLogService;

    @Inject
    public NewsService(NewsRepository repository, AccountService accountService, EventLogService eventLogService) {
        super(repository, new NewsConversion());
        this.accountService = accountService;
        this.eventLogService = eventLogService;
    }

    @Override
    public News create(News news) {
        Account author = accountService.findOne(news.authorId);

        if (!author.enabled) {
            throw new IllegalStateException(String.format("Account %s is not active", author.username));
        }

        News newsToCreate = new News(
                Id.random(),
                Instant.now(),
                news.authorId,
                news.title,
                news.urlFriendlyTitle,
                news.description,
                news.content,
                news.source,
                null,
                NewsStatus.DRAFT);

        News createdNews = super.create(newsToCreate);

        eventLogService.create("A news has been created");

        return createdNews;
    }

    @Override
    public News update(News news) {
        if (news == null) {
            throw new NoSuchElementException();
        }

        BNews existingNews = repository.findOne(news.getId())
            .orElseThrow(() -> new NoSuchElementException(String.format("News %s doesn't exist", news.getId())));

        if (NewsStatus.valueOf(existingNews.getStatus()) != NewsStatus.DRAFT) {
            throw new IllegalStateException(String.format("News %s is not in draft, it can't be updated", news.getId()));
        }

        existingNews.setTitle(news.title);
        existingNews.setUrlFriendlyTitle(new UrlFriendlyString(news.title).value);
        existingNews.setDescription(news.description);
        existingNews.setContent(news.content);
        existingNews.setSource(news.source);

        return conversion.from(repository.save(existingNews));
    }

    @Override
    public boolean delete(String id) {
        News news = findOne(id);

        if (news.status != NewsStatus.DRAFT) {
            throw new IllegalStateException(String.format("News %s is not in draft, it can't be deleted", news.getId()));
        }

        boolean deleted = repository.delete(id);

        eventLogService.create(String.format("The news %s has been deleted", news.getId()));

        return deleted;
    }

    public News markAsDraft(String id) {
        News updatedNews = updatePublication(id, NewsStatus.DRAFT, null);

        eventLogService.create(String.format("The news %s has been put in draft", updatedNews.getId()));

        return updatedNews;
    }

    public News markAsValidated(String id) {
        News updatedNews = updatePublication(id, NewsStatus.VALIDATED, null);

        eventLogService.create(String.format("The news %s has been validated for publication", updatedNews.getId()));

        return updatedNews;
    }

    public News markAsPublished(String id) {
        News updatedNews = updatePublication(id, NewsStatus.PUBLISHED, Instant.now());

        eventLogService.create(String.format("The news %s has been published", updatedNews.getId()));

        return updatedNews;
    }

    public Map<News, Account> authorByNews(Collection<String> newsIds) {
        Collection<News> news = findAll(QNews.news.id.in(newsIds));
        Set<String> accountIds = news.stream().map(newsItem -> newsItem.authorId).collect(toSet());
        Map<String, Account> accountById = accountService.findAll(QAccount.account.id.in(accountIds)).stream().collect(toMap(Account::getId, Function.identity()));

        return news.stream()
            .collect(toMap(Function.identity(), newsItem -> accountById.get(newsItem.authorId)));
    }

    private News updatePublication(String id, NewsStatus newsStatus, Instant publicationDate) {
        BNews existingNews = repository.findOne(id).orElseThrow(() ->
                new NoSuchElementException(String.format("News %s doesn't exist", id)));

        existingNews.setStatus(newsStatus.name());
        existingNews.setPublicationDate(newsStatus == NewsStatus.PUBLISHED ? publicationDate : null);

        return conversion.from(repository.save(existingNews));
    }
}
