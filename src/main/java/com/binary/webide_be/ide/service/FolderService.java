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
import com.binary.webide_be.util.dto.ResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import static com.binary.webide_be.exception.message.ErrorMsg.*;
import static com.binary.webide_be.exception.message.ErrorMsg.PARENT_FILE_NOT_FOUND;
import static com.binary.webide_be.exception.message.SuccessMsg.*;
import static com.binary.webide_be.exception.message.SuccessMsg.DELETE_FILE_SUCCESS;

@Slf4j
@Service
@RequiredArgsConstructor
public class FolderService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserTeamRepository userTeamRepository;
    private final FileDataRepository fileDataRepository;

    @Transactional
    public ResponseDto<?> createFolder(CreateFolderRequestDto createFolderRequestDto, UserDetailsImpl userDetails) {
        // 유저가 해당 프로젝트에 속해 있는지 확인
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Project project = projectRepository.findById(createFolderRequestDto.getProjectId())
                .orElseThrow(() -> new CustomException(PROJECT_NOT_FOUND));

        UserTeam userTeam = userTeamRepository.findByUserAndTeam(user, project.getTeam())
                .orElseThrow(() -> new CustomException(USER_NOT_IN_PROJECT_TEAM));

        FileData parent = null;
        if (createFolderRequestDto.getParentId() != null) {
            parent = fileDataRepository.findById(createFolderRequestDto.getParentId())
                    .orElseThrow(() -> new CustomException(PARENT_FILE_NOT_FOUND));
        }

        FileData fileData = new FileData(createFolderRequestDto, project, parent);
        fileData = fileDataRepository.save(fileData);

        FileTreeResponseDto fileTreeResponseDto = new FileTreeResponseDto(fileData);

        CreateFolderResponseDto createFolderResponseDto = new CreateFolderResponseDto(project, fileTreeResponseDto);

        return ResponseDto.builder()
                .statusCode(CREATE_FOLDER_SUCCESS.getHttpStatus().value())
                .data(createFolderResponseDto)
                .build();
    }

    @Transactional
    public ResponseDto<?> updateFolderPath(Long folderId, UpdateFolderPathRequestDto updateFolderPathRequestDto, UserDetailsImpl userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Long projectId = updateFolderPathRequestDto.getProjectId();
        Long parentId = updateFolderPathRequestDto.getParentId();

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(PROJECT_NOT_FOUND));

        UserTeam userTeam = userTeamRepository.findByUserAndTeam(user, project.getTeam())
                .orElseThrow(() -> new CustomException(USER_NOT_IN_PROJECT_TEAM));

        FileData fileData = fileDataRepository.findById(folderId)
                .orElseThrow(() -> new CustomException(FILE_NOT_FOUND));

        FileData newParent = null;
        if (parentId != null) {
            newParent = fileDataRepository.findById(parentId).orElseThrow(
                    () -> new CustomException(PARENT_FILE_NOT_FOUND));

            if (newParent.getFileType() != FileTypeEnum.D) {
                throw new CustomException(PARENT_FILE_NOT_DIRECTORY);
            }
        }

        fileData.updateParent(newParent);
        fileDataRepository.save(fileData);

        FileTreeResponseDto fileTreeResponseDto = new FileTreeResponseDto(fileData);

        return ResponseDto.builder()
                .statusCode(UPDATE_FOLDER_PATH_SUCCESS.getHttpStatus().value())
                .data(new CreateFolderResponseDto(project, fileTreeResponseDto))
                .build();
    }

    @Transactional
    public ResponseDto<?> deleteFolder(Long projectId, FileData fileData, UserDetailsImpl userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(PROJECT_NOT_FOUND));

        UserTeam userTeam = userTeamRepository.findByUserAndTeam(user, project.getTeam())
                .orElseThrow(() -> new CustomException(USER_NOT_IN_PROJECT_TEAM));

        deleteChildren(fileData);

        return ResponseDto.builder()
                .statusCode(DELETE_FOLDER_SUCCESS.getHttpStatus().value())
                .message(DELETE_FOLDER_SUCCESS.getDetail())
                .build();
    }

    // 주어진 폴더와 그 폴더에 포함된 모든 하위 파일 및 폴더를 재귀적으로 삭제
    private void deleteChildren(FileData fileData) {
        // 현재 폴더의 하위 항목을 모두 조회
        // 현재 FileData의 fileId를 parent_id로 가지고 있는 모든 자식 FileData 객체들의 리스트를 가져옴
        List<FileData> children = fileData.getChildren();

        // 하위 항목들이 존재하면, 각 항목에 대해 재귀적으로 이 메서드를 호출하여 삭제
        for (FileData child : children) {
            deleteChildren(child);
        }

        // 하위 항목들의 삭제가 완료되면, 현재 폴더(파일) 삭제
        fileDataRepository.delete(fileData);
    }

    @Transactional
    public ResponseDto<?> updateFolderName(Long projectId, FileData fileData, String newName, UserDetailsImpl userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(PROJECT_NOT_FOUND));

        UserTeam userTeam = userTeamRepository.findByUserAndTeam(user, project.getTeam())
                .orElseThrow(() -> new CustomException(USER_NOT_IN_PROJECT_TEAM));

        fileData.updateName(newName);

        return ResponseDto.builder()
                .statusCode(UPDATE_FOLDER_NAME_SUCCESS.getHttpStatus().value())
                .message(UPDATE_FOLDER_NAME_SUCCESS.getDetail())
                .data(new UpdateFileDataNameResponseDto(fileData))
                .build();
    }
}
