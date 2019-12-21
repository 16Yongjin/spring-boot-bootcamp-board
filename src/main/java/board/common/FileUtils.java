package board.common;

import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import board.board.dto.BoardFileDto;
import board.board.entity.BoardFileEntity;

// @Component 어노테이션으로 스프링 빈으로 등록하면 @Autowired로 간편하게 불러올 수 있다.
@Component
public class FileUtils {
  public List<BoardFileDto> parseFileInfo(int boardIdx, MultipartHttpServletRequest multipartHttpServletRequest)
      throws Exception {
    if (ObjectUtils.isEmpty(multipartHttpServletRequest)) {
      return null;
    }

    List<BoardFileDto> fileList = new ArrayList<>();

    // 파일이 업로드될 폴더 생성
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
    ZonedDateTime current = ZonedDateTime.now();
    String path = "images/" + current.format(format);
    File file = new File(path);
    if (file.exists() == false) {
      file.mkdirs();
    }

    Iterator<String> iterator = multipartHttpServletRequest.getFileNames();

    String newFileName, originalFileExtension, contentType;

    while (iterator.hasNext()) {
      List<MultipartFile> list = multipartHttpServletRequest.getFiles(iterator.next());
      for (MultipartFile multipartFile : list) {
        if (multipartFile.isEmpty() == false) {
          // 파일 형식을 확인하고 이미지 확장자 지정
          // 근데 이 방식은 파일 위변조 확인 불가하므로 별도 라이브러리 사용하는게 좋음
          contentType = multipartFile.getContentType();
          if (ObjectUtils.isEmpty(contentType)) {
            break;
          } else {
            if (contentType.contains("image/jpeg")) {
              originalFileExtension = ".jpg";
            } else if (contentType.contains("image/png")) {
              originalFileExtension = ".png";
            } else if (contentType.contains("image/gif")) {
              originalFileExtension = ".gif";
            } else {
              break;
            }
          }

          // 파일이름 생성, 밀리초가 아닌 나노초 사용으로 이름 중복 방지
          newFileName = Long.toString(System.nanoTime()) + originalFileExtension;

          // 데이터베이스에 저장할 파일 정보를 BoardFileDto에 저장
          BoardFileDto boardFile = new BoardFileDto();
          boardFile.setBoardIdx(boardIdx);
          boardFile.setFileSize(multipartFile.getSize());
          boardFile.setOriginalFileName(multipartFile.getOriginalFilename());
          boardFile.setStoredFilePath(path + "/" + newFileName);
          fileList.add(boardFile);

          // 업로드된 파일을 새로운 이름으로 바꾸어 지정된 경로에 저장
          file = new File(path + "/" + newFileName);
          multipartFile.transferTo(file);
        }
      }
    }
    return fileList;
  }

  public List<BoardFileEntity> parseFileInfo(MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {
    if (ObjectUtils.isEmpty(multipartHttpServletRequest)) {
      return null;
    }

    List<BoardFileEntity> fileList = new ArrayList<>();

    // 파일이 업로드될 폴더 생성
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMdd");
    ZonedDateTime current = ZonedDateTime.now();
    String path = "images/" + current.format(format);
    File file = new File(path);
    if (file.exists() == false) {
      file.mkdirs();
    }

    Iterator<String> iterator = multipartHttpServletRequest.getFileNames();

    String newFileName, originalFileExtension, contentType;

    while (iterator.hasNext()) {
      List<MultipartFile> list = multipartHttpServletRequest.getFiles(iterator.next());
      for (MultipartFile multipartFile : list) {
        if (multipartFile.isEmpty() == false) {
          // 파일 형식을 확인하고 이미지 확장자 지정
          // 근데 이 방식은 파일 위변조 확인 불가하므로 별도 라이브러리 사용하는게 좋음
          contentType = multipartFile.getContentType();
          if (ObjectUtils.isEmpty(contentType)) {
            break;
          } else {
            if (contentType.contains("image/jpeg")) {
              originalFileExtension = ".jpg";
            } else if (contentType.contains("image/png")) {
              originalFileExtension = ".png";
            } else if (contentType.contains("image/gif")) {
              originalFileExtension = ".gif";
            } else {
              break;
            }
          }

          // 파일이름 생성, 밀리초가 아닌 나노초 사용으로 이름 중복 방지
          newFileName = Long.toString(System.nanoTime()) + originalFileExtension;

          // 데이터베이스에 저장할 파일 정보를 BoardFileDto에 저장
          BoardFileEntity boardFile = new BoardFileEntity();
          boardFile.setFileSize(multipartFile.getSize());
          boardFile.setOriginalFileName(multipartFile.getOriginalFilename());
          boardFile.setStoredFilePath(path + "/" + newFileName);
          boardFile.setCreatorId("admin");
          fileList.add(boardFile);

          // 업로드된 파일을 새로운 이름으로 바꾸어 지정된 경로에 저장
          file = new File(path + "/" + newFileName);
          multipartFile.transferTo(file);
        }
      }
    }
    return fileList;
  }
}