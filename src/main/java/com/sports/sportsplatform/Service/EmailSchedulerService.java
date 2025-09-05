package com.sports.sportsplatform.Service;



import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class EmailSchedulerService {

    private final UserSettingsService userSettingsService;

    // Initialize TaskScheduler inside this service
    private final TaskScheduler scheduler = createScheduler();

    // Method to schedule emails
    public void scheduleEventEmail(Long eventId, long delayInMinutes) {
        scheduler.schedule(
                () -> userSettingsService.sendEventEmails(eventId),
                Instant.now().plusSeconds(delayInMinutes * 60)
        );
    }

    // Create TaskScheduler bean programmatically
    private TaskScheduler createScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(5);
        taskScheduler.setThreadNamePrefix("scheduler-");
        taskScheduler.initialize();
        return taskScheduler;
    }
}



