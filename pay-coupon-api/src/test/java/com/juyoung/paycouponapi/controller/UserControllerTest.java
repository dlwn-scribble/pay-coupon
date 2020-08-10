package com.juyoung.paycouponapi.controller;

import com.juyoung.paycouponapi.model.entity.User;
import com.juyoung.paycouponapi.security.TokenProvider;
import com.juyoung.paycouponapi.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserController userController;

    @MockBean
    UserServiceImpl userService;

    @MockBean
    TokenProvider tokenProvider;

    @Mock
    User mockUser = new User();

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    void Should_Be_Unauthorized_For_Signin_Invalid_User() throws Exception {
        when(userService.getUserByUserIdAndPassword(any(String.class), any(String.class)))
                .thenReturn(null);
        mockMvc.perform(post("/api/v1/signin")
                .param("id", "test")
                .param("pw", "test")).andExpect(status().isUnauthorized());
    }

    @Test
    void Should_Be_Created_For_Signup() throws Exception {
        when(mockUser.getUserId()).thenReturn("TEST_USER");
        when(userService.createUser(any(String.class), any(String.class)))
                .thenReturn(mockUser);
        when(tokenProvider.createToken(any(String.class), any(List.class))).thenReturn("TOKEN");
        when(tokenProvider.getTokenType()).thenReturn("TOKEN_TYPE");
        mockMvc.perform(post("/api/v1/signup")
                .param("id", "test")
                .param("pw", "test")).andExpect(status().isCreated());
    }
}