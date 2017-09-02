package com.keke.bootext.data.mybatis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mybatis.annotations.Entity;
import org.springframework.data.mybatis.domains.LongId;
import org.springframework.data.mybatis.repository.support.MybatisRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.config.Projection;

@SpringBootApplication
public class BootextDataMybatisApplication {
    public static void main(String[] args) {
        SpringApplication.run(BootextDataMybatisApplication.class, args);
    }

}

@RepositoryRestResource(excerptProjection = UserDataProjection.class)
interface ReservationRepository extends MybatisRepository<UserData, Long> {
}

@Projection(name = "userDataProjection", types = {UserData.class})
interface UserDataProjection {
    String getName();
}

@Entity
class UserData extends LongId {

    private String name;
    private String age;

    public UserData(String name, String age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
