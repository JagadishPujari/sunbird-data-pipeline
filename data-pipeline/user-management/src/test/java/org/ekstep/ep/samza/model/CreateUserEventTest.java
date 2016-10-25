package org.ekstep.ep.samza.model;

import com.google.gson.JsonSyntaxException;
import com.zaxxer.hikari.HikariDataSource;
import org.ekstep.ep.samza.fixtures.EventFixture;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.text.ParseException;


public class CreateUserEventTest {
    private HikariDataSource dataSource;


    @Before
    public void setUp(){
        String url = String.format("jdbc:mysql://%s:%s/%s", "localhost", "3306", "ecosystem");
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername("jenkins");
        dataSource.setPassword("password");
    }

    @Test
    public void ShouldProcessEventAndInsertNewEntry() throws SQLException, ParseException {
        Event event = new Event(new EventFixture().CREATE_USER_EVENT);

        CreateLearnerDto learnerDto = new CreateLearnerDto(dataSource);
        learnerDto.process(event);

        Assert.assertEquals(true,learnerDto.getIsInserted());
    }

    @Test(expected=ParseException.class)
    public void ShouldNotInsertIfUidIsNull() throws SQLException, ParseException {
        Event event = new Event(new EventFixture().INVALID_CREATE_EVENT);
        CreateLearnerDto learnerDto = new CreateLearnerDto(dataSource);
        learnerDto.process(event);
    }

}
