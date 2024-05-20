package com.beanions.common.uploadfiles;

import com.beanions.common.dto.FilesDTO;
import com.beanions.common.dto.PostDTO;
import com.beanions.mypage.dao.UploadMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UploadService {
  private final UploadMapper uploadMapper;

  public UploadService(UploadMapper uploadMapper) {this.uploadMapper = uploadMapper;}

  public List<FilesDTO> registWriting() {return uploadMapper.registWriting();}

  public List<FilesDTO> selectAllFiles() {return uploadMapper.selectAllFiles();}

  public int registPost(PostDTO post) {
    return uploadMapper.registPost(post);
  }

  public PostDTO selectPost(int membercode) {
    return uploadMapper.selectPost(membercode);
  }

  public void insertFile(FilesDTO fileInfo) {
    uploadMapper.insertFile(fileInfo);
  }


  //public List<FilesDTO> registerFileSelected() {return uploadMapper.registerFileSelected();}
}
