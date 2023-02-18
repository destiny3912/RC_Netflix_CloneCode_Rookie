package com.example.demo.src.Auth;

import com.example.demo.src.Auth.Model.PostLoginReq;
import com.example.demo.src.Auth.Model.PostLoginRes;
import com.example.demo.src.Auth.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class AuthDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public User postLogin(PostLoginReq postLoginReq) {
        String getInfoQuery = "select userIdx from User where ID = ? and PW = ?";

        Object[] getInfoParams = new Object[] {
                postLoginReq.getUserId(),
                postLoginReq.getUserPassword()
        };

        return this.jdbcTemplate.queryForObject(getInfoQuery,(rs, rowNum)
                -> new User(
                        rs.getInt("userIdx")
        ), getInfoParams);
    }
}
