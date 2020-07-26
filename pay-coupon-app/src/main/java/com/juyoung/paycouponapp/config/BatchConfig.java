package com.juyoung.paycouponapp.config;

import com.juyoung.paycouponapp.CouponItemReader;
import com.juyoung.paycouponapp.CouponItemWriter;
import com.juyoung.paycouponapp.model.entity.Coupon;
import com.juyoung.paycouponapp.repository.CouponRepository;
import com.juyoung.paycouponapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class BatchConfig {

    @Autowired
    CouponRepository couponRepository;

    @Autowired
    UserRepository userRepository;

    private static final String JOB_NAME = "alarmPagingJob";
    private final StepBuilderFactory stepBuilderFactory;
    private final JobBuilderFactory jobBuilderFactory;

    private final int chunkSize = 2;

    @Bean
    public Job alarmPagingJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .start(couponPagingStep())
                .build();
    }

    @Bean
    @JobScope
    public Step couponPagingStep() {
        return stepBuilderFactory.get("couponPagingStep")
                .<Coupon, Coupon>chunk(chunkSize)
                .reader(new CouponItemReader(couponRepository,3, chunkSize))
                .processor(couponProcessor())
                .writer(new CouponItemWriter(couponRepository, userRepository))
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<Coupon, Coupon> couponProcessor() {
        return item -> {
            return item;
        };
    }



}
