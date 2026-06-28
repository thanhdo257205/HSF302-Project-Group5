package vn.edu.fpt.hsf302_group5.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean
    public Executor mailExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); // số luồng tối thiểu pool luôn cố gắng duy trì
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(100); // đưa các task cần chờ vào hàng đợi, đợi các task trước đó xử lí xong
        executor.setThreadNamePrefix("mail-"); // đặt để ghi log, vd log.info(Thread.currentThread().getName())
        executor.initialize(); // Khởi động thread pool
//        1. Nếu số thread hiện tại < corePoolSize
//           -> tạo thread mới
//        2. Nếu đã đủ corePoolSize
//           -> đưa task vào queue
//        3. Nếu queue đầy và số thread hiện tại < maxPoolSize
//           -> tạo thêm thread mới
//        4. Nếu queue đầy và đã đạt maxPoolSize
//           -> từ chối task
        return executor;
    }
}
