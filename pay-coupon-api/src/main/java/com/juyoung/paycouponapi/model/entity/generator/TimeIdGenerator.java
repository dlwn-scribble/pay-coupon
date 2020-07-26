package com.juyoung.paycouponapi.model.entity.generator;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.stream.Collectors;

public class TimeIdGenerator implements IdentifierGenerator {

    static final Logger logger = LoggerFactory.getLogger(TimeIdGenerator.class);

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {

        try{
            //서버 날짜 기준 으로 생성
            LocalDateTime time = LocalDateTime.now();

            DateTimeFormatter formatter =DateTimeFormatter.ofPattern("HHmmss-ddMMyyyy");
            String id = String.format("%s-%s", getRandomString(5), time.format(formatter));
            return id;
        }
        catch (Exception e){
            logger.error("fail to generate coupon ID {}", e.getMessage(), e);
            throw e;
        }
    }

    //TODO : 성능 확인
    private String getRandomString(int length){
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        return new Random().ints(length, 0, chars.length())
                .mapToObj(i -> "" + chars.charAt(i))
                .collect(Collectors.joining());

    }
}
