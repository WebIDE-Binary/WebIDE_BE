package com.binary.webide_be.ide.service;

import com.binary.webide_be.exception.CustomException;
import com.binary.webide_be.ide.dto.*;
import com.binary.webide_be.ide.entity.FileData;
import com.binary.webide_be.ide.entity.FileTypeEnum;
import com.binary.webide_be.ide.repository.FileDataRepository;
import com.binary.webide_be.project.entity.Project;
import com.binary.webide_be.project.repository.ProjectRepository;
import com.binary.webide_be.security.UserDetailsImpl;
import com.binary.webide_be.team.entity.UserTeam;
import com.binary.webide_be.team.repository.UserTeamRepository;
import com.binary.webide_be.user.entity.User;
import com.binary.webide_be.user.repository.UserRepository;
import com.binary.webide_be.util.S3Service;
import com.binary.webide_be.util.dto.ResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.binary.webide_be.exception.message.ErrorMsg.*;
import static com.binary.webide_be.exception.message.SuccessMsg.*;

@Service
@RequiredArgsConstructor
public class FileService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserTeamRepository userTeamRepository;
    private final FileDataRepository fileDataRepository;
    private final S3Service s3Service;

    @Transactional
    public ResponseDto<?> createFile(CreateFileRequestDto createFileRequestDto, UserDetailsImpl userDetails) {
        // 유저가 해당 프로젝트에 속해 있는지 확인
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Project project = projectRepository.findById(createFileRequestDto.getProjectId())
                .orElseThrow(() -> new CustomException(PROJECT_NOT_FOUND));

        UserTeam userTeam = userTeamRepository.findByUserAndTeam(user, project.getTeam())
                .orElseThrow(() -> new CustomException(USER_NOT_IN_PROJECT_TEAM));

        FileData parent = null;
        if (createFileRequestDto.getParentId() != null) {
            parent = fileDataRepository.findById(createFileRequestDto.getParentId())
                    .orElseThrow(() -> new CustomException(PARENT_FILE_NOT_FOUND));
        }

        FileData fileData = new FileData(createFileRequestDto, project, parent);
        fileData = fileDataRepository.save(fileData);

        FileTreeResponseDto fileTreeResponseDto = new FileTreeResponseDto(fileData);

        CreateFileResponseDto createFileResponseDto = new CreateFileResponseDto(project, fileTreeResponseDto);

        return ResponseDto.builder()
                .statusCode(CREATE_FILE_SUCCESS.getHttpStatus().value())
                .data(createFileResponseDto)
                .build();
    }

    @Transactional
    public ResponseDto<?> updateFilePath(Long fileId, UpdateFilePathRequestDto updateFilePathRequestDto, UserDetailsImpl userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Long projectId = updateFilePathRequestDto.getProjectId();
        Long parentId = updateFilePathRequestDto.getParentId();

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(PROJECT_NOT_FOUND));

        UserTeam userTeam = userTeamRepository.findByUserAndTeam(user, project.getTeam())
                .orElseThrow(() -> new CustomException(USER_NOT_IN_PROJECT_TEAM));

        // 파일 정보 가져오기
        FileData fileData = fileDataRepository.findById(fileId)
                .orElseThrow(() -> new CustomException(FILE_NOT_FOUND));

        FileData newParent = null;
        if (updateFilePathRequestDto.getParentId() != null) {
            newParent = fileDataRepository.findById(parentId).orElseThrow(
                    () -> new CustomException(PARENT_FILE_NOT_FOUND)
            );

            if (newParent.getFileType() != FileTypeEnum.D) {
                throw new CustomException(PARENT_FILE_NOT_DIRECTORY);
            }
        }

        fileData.updateParent(newParent);
        fileDataRepository.save(fileData);

        FileTreeResponseDto fileTreeResponseDto = new FileTreeResponseDto(fileData);


        return ResponseDto.builder()
                .statusCode(UPDATE_FILE_PATH_SUCCESS.getHttpStatus().value())
                .message(UPDATE_FILE_PATH_SUCCESS.getDetail())
                .data(new CreateFileResponseDto(project, fileTreeResponseDto))
                .build();
    }

    @Transactional
    public ResponseDto<?> deleteFile(Long projectId, FileData fileData, UserDetailsImpl userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(PROJECT_NOT_FOUND));

        UserTeam userTeam = userTeamRepository.findByUserAndTeam(user, project.getTeam())
                .orElseThrow(() -> new CustomException(USER_NOT_IN_PROJECT_TEAM));

        fileDataRepository.delete(fileData);

        return ResponseDto.builder()
                .statusCode(DELETE_FILE_SUCCESS.getHttpStatus().value())
                .message(DELETE_FILE_SUCCESS.getDetail())
                .build();
    }

    @Transactional
    public ResponseDto<?> updateFileName(Long projectId, FileData fileData, String newName, UserDetailsImpl userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(PROJECT_NOT_FOUND));

        UserTeam userTeam = userTeamRepository.findByUserAndTeam(user, project.getTeam())
                .orElseThrow(() -> new CustomException(USER_NOT_IN_PROJECT_TEAM));

        fileData.updateName(newName);

        return ResponseDto.builder()
                .statusCode(UPDATE_FILENAME_SUCCESS.getHttpStatus().value())
                .message(UPDATE_FILENAME_SUCCESS.getDetail())
                .data(new UpdateFileDataNameResponseDto(fileData))
                .build();
    }

    @Transactional
    public ResponseDto<?> updateCodeFile(Long fileId, MultipartFile codeFile, UpdateFileContentRequestDto updateFileContentRequestDto, UserDetailsImpl userDetails) throws IOException {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Project project = projectRepository.findById(updateFileContentRequestDto.getProjectId())
                .orElseThrow(() -> new CustomException(PROJECT_NOT_FOUND));

        UserTeam userTeam = userTeamRepository.findByUserAndTeam(user, project.getTeam())
                .orElseThrow(() -> new CustomException(USER_NOT_IN_PROJECT_TEAM));

        FileData fileData = fileDataRepository.findById(fileId).orElseThrow(
                () -> new CustomException(FILE_NOT_FOUND)
        );

        String s3FileUrl = null;
        if (codeFile != null && !codeFile.isEmpty()) {
            s3FileUrl = s3Service.uploadFile(codeFile, "code_files");
        }

        fileData.updateS3Address(s3FileUrl);
        fileDataRepository.save(fileData);

        return ResponseDto.builder()
                .statusCode(UPDATE_FILE_CONTENT_SUCCESS.getHttpStatus().value())
                .message(UPDATE_FILE_CONTENT_SUCCESS.getDetail())
                .data(new UpdateFileContentResponseDto(fileData.getFileS3Address()))
                .build();
    }

}
