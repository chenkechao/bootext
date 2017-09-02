package com.keke.bootext.data.mybatis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mybatis.annotations.Entity;
import org.springframework.data.mybatis.domains.LongId;
import org.springframework.data.mybatis.repository.support.MybatisRepository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BootextDataMybatisApplication.class)
public class BootextDataMybatisApplicationText {

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    public void testDataSave() {
        reservationRepository.save(new UserData("11","22"));
    }
}
