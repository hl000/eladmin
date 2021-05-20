package me.zhengjie.schedule;

import lombok.extern.slf4j.Slf4j;
import me.zhengjie.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;

/**
 * @author HL
 * @create 2021/5/13 16:08
 */
@Slf4j
@Component
public class DailyPlanTimmer {

    @Autowired
    private PlanService planService;

    @Scheduled(cron = "${scheduled.dailyPlan-timer}")
    public void createDailyPlanByTimer() throws ParseException {
        planService.createDailyPlan();
    }
}
