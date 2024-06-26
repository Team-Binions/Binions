package com.beanions.mypage.service;

import com.beanions.common.dto.MembersDTO;
import com.beanions.mypage.dao.MyPageMapper;
import com.beanions.mypage.dto.MyPageDTO;
import com.beanions.mypage.dto.SchedulesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MyPageService {

  @Autowired
  private final MyPageMapper myPageMapper;
  @Autowired
  private final PasswordEncoder passwordEncoder;

  public MyPageService(MyPageMapper myPageMapper, PasswordEncoder passwordEncoder) {
    this.myPageMapper = myPageMapper;
      this.passwordEncoder = passwordEncoder;
  }

  public List<MyPageDTO> selectAllMyPageData() {
    return myPageMapper.selectAllMyPageData();
  }

  public List<MyPageDTO> selectMyPageMainData(int memberCode) {
    return myPageMapper.selectMyPageMainData(memberCode);
  }

  public List<MyPageDTO> selectMyPageReviewData(int memberCode) { return myPageMapper.selectMyPageReviewData(memberCode);}

  public List<MyPageDTO> selectMyPageFreeData(int memberCode) { return myPageMapper.selectMyPageFreeData(memberCode);}

  public List<MyPageDTO> selectMyPageCommentData(int memberCode) {return myPageMapper.selectMyPageCommentData(memberCode);}

  public List<MyPageDTO> selectMyPageCommentPostCategory(int memberCode) {return myPageMapper.selectMyPageCommentPostCategory(memberCode);}

  @Transactional
  public void registNewSchedule(SchedulesDTO newSchedule) {
    myPageMapper.insertNewSchedule(newSchedule);
  }

  public List<MyPageDTO> selectMyPageScheduleInfo(int memberCode) {
    return myPageMapper.selectMyPageScheduleInfo(memberCode);
  }

  public List<MyPageDTO> selectScheduleDetail(int memberCode,String id) {
    int code = Integer.parseInt(id);
    return myPageMapper.selectScheduleDetail(memberCode,code);
  }

  @Transactional
  public void modifySchedule(SchedulesDTO modifiedSchedule) { myPageMapper.modifySchedule(modifiedSchedule);
  }

  @Transactional
  public void deleteSchedule(int code) {myPageMapper.deleteSchedule(code);}

  public MyPageDTO selectMyPostInfo(int memberCode) {
    return myPageMapper.selectMyPostInfo(memberCode);
  }

  @Transactional
  public void registWriting() {myPageMapper.registWriting();}

  public int modifyMemberInfo(MembersDTO member) {

      int result = 0;

      member.setMemberPw(passwordEncoder.encode(member.getMemberPw()));

      result = myPageMapper.modifyMemberInfo(member);

      if(result > 0 ) {
        return result;
      }
      return result;
  }

    public void deleteMember(Integer memberCode) { myPageMapper.deleteMember(memberCode);}
}
