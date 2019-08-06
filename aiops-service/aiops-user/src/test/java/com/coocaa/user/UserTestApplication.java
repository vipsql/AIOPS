package com.coocaa.user;

/**
 * @program: intelligent_maintenance
 * @description:
 * @author: dongyang_wu
 * @create: 2019-07-31 11:05
 */

import com.coocaa.user.entity.User;
import com.coocaa.user.mapper.UserMapper;
import lombok.AllArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @program: intelligent_maintenance
 * @description:
 * @author: dongyang_wu
 * @create: 2019-07-30 17:31
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes={UserApplication.class})
public class UserTestApplication {
    @Autowired
    private UserMapper userMapper;
    @Test
    public void testForeignKey(){
        User user = userMapper.getUser(1L);
        System.out.println(user);
    }
}