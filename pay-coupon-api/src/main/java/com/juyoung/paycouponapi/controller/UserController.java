package com.juyoung.paycouponapi.controller;

import com.juyoung.paycouponapi.model.entity.User;
import com.juyoung.paycouponapi.model.response.TokenResponse;
import com.juyoung.paycouponapi.security.TokenProvider;
import com.juyoung.paycouponapi.service.UserServiceImpl;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RequestMapping("/api/v1")
@RestController
public class UserController {
    @Value("${spring.jwt.header}")
    private String header;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    UserServiceImpl userService;

    @ApiOperation(value = "로그인", notes = "아이디, 비밀 번호를 입력 하세요.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "로그인 성공"),
            @ApiResponse(code = 401, message = "인증 실퍠"),
            @ApiResponse(code = 500, message = "시스템 에러"),
    })
    @PostMapping("/signin")
    public ResponseEntity<TokenResponse> signin(@ApiParam(value = "회원 ID", required = true) @RequestParam("id")  String id,
                                                @ApiParam(value = "비밀 번호", required = true) @RequestParam("pw")  String pw){
        if(userService.getUserByUserIdAndPassword(id, pw)!=null){
            TokenResponse res = new TokenResponse(tokenProvider.getTokenType(), tokenProvider.createToken(id, Collections.singletonList("ROLE_USER")));
            return new ResponseEntity<TokenResponse>(res, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<TokenResponse>(new TokenResponse("",""), HttpStatus.UNAUTHORIZED);
        }
    }
    @ApiOperation(value = "가입", notes = "신규 회원을 등록 하세요.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "계정 생성"),
            @ApiResponse(code = 500, message = "시스템 에러"),
    })
    @PostMapping("/signup")
    public ResponseEntity<TokenResponse> signup(@ApiParam(value = "회원 ID", required = true) @RequestParam("id")  String id,
                                                @ApiParam(value = "비밀 번호", required = true) @RequestParam("pw") String pw){

        User user = userService.createUser(id, pw);
        TokenResponse res = new TokenResponse(tokenProvider.getTokenType(), tokenProvider.createToken(user.getUserId(), Collections.singletonList("ROLE_USER")));
        return new ResponseEntity<TokenResponse>(res, HttpStatus.CREATED);
    }
}
