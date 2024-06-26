package com.beanions.user.service;

import com.beanions.auth.dao.UserMapper;
import com.beanions.common.dto.MembersDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
@EnableScheduling
public class SignupService {

    @Autowired
    private final UserMapper userMapper;

    @Autowired
    private final PasswordEncoder passwordEncoder;


    public int checkDupId(String id) {
        return userMapper.checkDupId(id);
    }

    public int checkDupNkname(String nkname) {
        return userMapper.checkDupNkname(nkname);
    }

    public void regist(MembersDTO member) {

        member.setMemberPw(passwordEncoder.encode(member.getMemberPw()));
        System.out.println(member);
        userMapper.joinMember(member);
    }

    public int checkDupEmail(String email) {
        return userMapper.checkDupEmail(email);
    }

    @Scheduled(fixedRate = 300000) // ms 기준 / 매번 약 5분마다 실행
    public void deleteSignupFile() {
        Path directory = Path.of("src/main/resources/assets/images/upload/user/signupTemp");

        if (directory != null && Files.isDirectory(directory)) {
            try {
                LocalDateTime now = LocalDateTime.now();
                String formatedNow = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-EE HH:mm:ss"));

                Files.walk(directory)
                        .filter(Files::isRegularFile)
                        .map(Path::toFile)
                        .forEach(File::delete);
                System.out.println(formatedNow + " --- 회원가입 임시저장 업로드 파일이 삭제되었습니다...");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error occurred while deleting files.");
            }
        } else {
            System.out.println("Directory not found.");
        }
    }

}
