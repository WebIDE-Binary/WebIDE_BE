package com.binary.webide_be.ide.service;

import com.binary.webide_be.exception.CustomException;
import com.binary.webide_be.ide.dto.FileTreeResponseDto;
import com.binary.webide_be.ide.dto.IdeResponseDto;
import com.binary.webide_be.ide.dto.UpdateFileDataNameRequestDto;
import com.binary.webide_be.ide.entity.FileData;
import com.binary.webide_be.ide.entity.FileTypeEnum;
import com.binary.webide_be.ide.repository.FileDataRepository;
import com.binary.webide_be.project.entity.Project;
import com.binary.webide_be.project.repository.ProjectRepository;
import com.binary.webide_be.security.UserDetailsImpl;
import com.binary.webide_be.team.entity.Team;
import com.binary.webide_be.team.entity.UserTeam;
import com.binary.webide_be.team.repository.TeamRepository;
import com.binary.webide_be.team.repository.UserTeamRepository;
import com.binary.webide_be.user.entity.User;
import com.binary.webide_be.user.repository.UserRepository;
import com.binary.webide_be.util.dto.ResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.binary.webide_be.exception.message.ErrorMsg.*;
import static com.binary.webide_be.exception.message.SuccessMsg.CHAT_HISTORY_SUCCESS;
import static com.binary.webide_be.exception.message.SuccessMsg.FILE_TREE_SUCCESS;

@Service
@RequiredArgsConstructor
public class IdeService {
    private final ProjectRepository projectRepository;
    private final FileDataRepository fileDataRepository;
    private final UserRepository userRepository;
    private final UserTeamRepository userTeamRepository;
    private final TeamRepository teamRepository;
    private final FileService fileService;
    private final FolderService folderService;

    //폴더/파일 구조 불러오기
    public ResponseDto<?> getFileTree(Long projectId, UserDetailsImpl userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new CustomException(PROJECT_NOT_FOUND));

        UserTeam userTeam = userTeamRepository.findByUserAndTeam(user, project.getTeam())
                .orElseThrow(() -> new CustomException(USER_NOT_IN_PROJECT_TEAM));

        List<FileData> fileDataList = fileDataRepository.findAllByProjectId(project);

        List<FileTreeResponseDto> fileTreeResponseDtoList = new ArrayList<>();

        for (FileData fileData : fileDataList) {
            fileTreeResponseDtoList.add(new FileTreeResponseDto(fileData));
        }

        IdeResponseDto ideResponseDto = new IdeResponseDto(project, fileTreeResponseDtoList);

        return ResponseDto.builder()
                .statusCode(FILE_TREE_SUCCESS.getHttpStatus().value())
                .data(ideResponseDto)
                .build();
    }

    //파일 삭제
    @Transactional
    public ResponseDto<?> deleteFileData(Long projectId, Long fileDateId, UserDetailsImpl userDetails) {
        FileData fileData = fileDataRepository.findById(fileDateId).orElseThrow(
                () -> new CustomException(FILE_NOT_FOUND)
        );

        if (fileData.getFileType() == FileTypeEnum.D) {
            return folderService.deleteFolder(projectId, fileData, userDetails);
        } else {
            return fileService.deleteFile(projectId, fileData, userDetails);
        }

    }

    //파일 이름 변경
    @Transactional
    public ResponseDto<?> updateFileDataName(Long projectId, Long fileDateId, UpdateFileDataNameRequestDto updateFileDataNameRequestDto, UserDetailsImpl userDetails) {
        FileData fileData = fileDataRepository.findById(fileDateId).orElseThrow(
                () -> new CustomException(FILE_NOT_FOUND)
        );

        String newName = updateFileDataNameRequestDto.getNewName();

        if (fileData.getFileType() == FileTypeEnum.D) {
            return folderService.updateFolderName(projectId, fileData, newName, userDetails);
        } else {
            return fileService.updateFileName(projectId, fileData, newName, userDetails);
        }
    }
}
