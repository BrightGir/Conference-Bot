package ru.bright.bot.service.seminars;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.bright.bot.model.ScienceSeminar;
import ru.bright.bot.model.User;
import ru.bright.bot.model.dto.SeminarDTO;
import ru.bright.bot.service.managers.SeminarsManager;
import ru.bright.bot.service.managers.UserManager;

import java.text.DecimalFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.*;

@Component
@Slf4j
public class SeminarsPagesStore implements UpdateSeminarsListener, ParticipateUserListener {

    private SeminarsManager seminarsManager;
    private UserManager userManager;

    private Map<String,List<String>> categoryPages;
    private Map<Long,List<String>> pagesByOwnerId;
    private Map<Long,List<String>> joinedPagesByOwnerId;
    private Map<Long,List<String>> pagesBySeminarPrimaryIdInfo;
    private static int MAX_LINES = 20;

    public SeminarsPagesStore(@Autowired SeminarsManager seminarsManager, @Autowired UserManager userManager) {
        this.seminarsManager = seminarsManager;
        this.userManager = userManager;
        this.categoryPages = new HashMap<>();
        this.pagesByOwnerId = new HashMap<>();
        this.joinedPagesByOwnerId = new HashMap<>();
        this.pagesBySeminarPrimaryIdInfo = new HashMap<>();
    }

    @PostConstruct
    public void init() {
  //      initPages();
    //    initSeminarInfoAll();
        registerClassAsListener();
    }

    public String getPage(String category, int page) {
        initPages();
        return categoryPages.get(category).get(page-1);
    }

    public List<String> getPagesByOwnerId(Long chatId) {
        initPagesByOwnerId(chatId);
        return pagesByOwnerId.get(chatId);
    }

    public List<String> getJoinedPages(Long chatId) {
        initJoinedPages(chatId);
        return joinedPagesByOwnerId.get(chatId);
    }

    public List<String> getSeminarInfoPages(long id) {
        return pagesBySeminarPrimaryIdInfo.get(id);
    }

    private void initSeminarInfoAll() {
        seminarsManager.getAllSeminars().forEach(seminar -> {
            initInfoPageForSeminar(seminar.getId());
        });
    }

    private void registerClassAsListener() {
        seminarsManager.registerUpdateSeminarsListener(this);
        seminarsManager.registerParticipateUserListener(this);
    }

    private void initPages() {
        for(SeminarCategory category: SeminarCategory.values()) {
            initPagesByCategory(category.toString());
        }
    }

    private void initPagesByCategory(String category) {
        List<String> pages = new ArrayList<>();
        List<SeminarDTO> seminars = seminarsManager.getByCategory(category);
        String currentPage = "";
        int str = 0;
        for (SeminarDTO seminar : seminars) {
            int addStr = 4 + seminar.getAdditionalInformation().split("\n").length;
            if (str + addStr > MAX_LINES) {
                pages.add(currentPage);
                currentPage = "";
                str = 0;
            }

            currentPage += seminarInfo(seminar);
            currentPage += "\n";
            currentPage += seminar.getAdditionalInformation();
            currentPage += "\n\n";
            str += addStr;
        }
        if(!currentPage.isEmpty()) {
            pages.add(currentPage);
        } else {
            if(pages.size() == 0) {
                pages.add("Семинаров нет.");
            }
        }
        categoryPages.put(category,pages);
    }

    private void initPagesByOwnerId(Long chatId) {
        List<String> pages = new ArrayList<>();
        List<SeminarDTO> seminars = seminarsManager.getSeminarsByChatIdOwner(chatId);

        String currentPage = "";
        int str = 0;
        for (SeminarDTO seminar : seminars) {
            int addStr = 4;
            if (str + addStr > MAX_LINES) {
                pages.add(currentPage);
                currentPage = "";
                str = 0;
            }
            currentPage += seminarInfo(seminar);
            currentPage += "\n\n";
            str += addStr;
        }
        if(!currentPage.isEmpty()) {
            pages.add(currentPage);
        } else {
            if(pages.size() == 0) {
                pages.add("У вас нет семинаров.");
            }
        }
        pagesByOwnerId.put(chatId,pages);
    }

    private String seminarInfo(SeminarDTO seminar) {
        String info = "";
        info += "ID = " + seminar.getId() + "\n";
        info += "*" + seminar.getName() + "*";
        info += "\n";
        info += seminar.getLeaderFIO();
        info += "\n";
        ZonedDateTime zdt = seminar.getTimestamp().toInstant()
                .atZone(ZoneId.of("Europe/Moscow"));
        String monthString = zdt.getMonth().getDisplayName(TextStyle.FULL,new Locale("ru"));
        info += "_" + zdt.getDayOfMonth() + " " + monthString.substring(0,1).toUpperCase() + monthString.substring(1) + " " + zdt.getYear() + " " +
                zdt.getHour()  + ":" + new DecimalFormat("00").format(zdt.getMinute())  + "_";
        return info;
    }

    private void initInfoPageForSeminar(long id) {
        ScienceSeminar seminar = seminarsManager.findById(id);
        List<String> pages = new ArrayList<>();
        String currentPage = "";
        int addStr = 4 + seminar.getAdditionalInformation().split("\n").length;
        currentPage += seminarInfo(SeminarDTO.from(seminar));
        currentPage += "\n";
        currentPage += seminar.getAdditionalInformation();
        currentPage += "\n\n";
        currentPage += "Участники (" + seminar.getParticipants().size() + "):\n";
        for(User user: seminar.getParticipants()) {
            if(addStr > MAX_LINES) {
                pages.add(currentPage);
                currentPage = "";
                addStr = 0;
            }
            currentPage += user.getFio() + "\n";
            addStr++;
        }
        if(!currentPage.isEmpty()) {
            pages.add(currentPage);
        }
        pagesBySeminarPrimaryIdInfo.put(id,pages);
    }

    private void initJoinedPages(Long chatId) {
        List<String> pages = new ArrayList<>();
        List<SeminarDTO> seminars = seminarsManager.getSeminarsByUserId(chatId);

        String currentPage = "";
        int str = 0;
        for (SeminarDTO seminar : seminars) {
            int addStr = 4 + seminar.getAdditionalInformation().split("\n").length;
            if (str + addStr > MAX_LINES) {
                pages.add(currentPage);
                currentPage = "";
                str = 0;
            }

            currentPage += seminarInfo(seminar);
            currentPage += "\n";
            currentPage += seminar.getAdditionalInformation();
            currentPage += "\n\n";
            str += addStr;
        }
        if(!currentPage.isEmpty()) {
            pages.add(currentPage);
        } else {
            if(pages.size() == 0) {
                pages.add("Вы не участвуете ни в одном семинаре.");
            }
        }
        joinedPagesByOwnerId.put(chatId,pages);
    }


    @Override
    public void update(ScienceSeminar seminar, boolean add) {
        initPagesByCategory(seminar.getSeminarCategoryEnum());
        initPagesByOwnerId(seminar.getChatIdOwner());
        if(!add) {
            for(User user: seminar.getParticipants()) {
                initJoinedPages(user.getChatId());
            }
        } else {
            initInfoPageForSeminar(seminar.getId());
        }
    }

    public int getMaxPages(String category) {
        return categoryPages.get(category).size();
    }

    @Override
    public void participateUser(User user, ScienceSeminar seminar) {
        initJoinedPages(user.getChatId());
        initInfoPageForSeminar(seminar.getId());
    }
}
