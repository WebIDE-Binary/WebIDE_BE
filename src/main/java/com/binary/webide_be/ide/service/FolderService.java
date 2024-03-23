package com.binary.webide_be.ide.service;

import com.binary.webide_be.exception.CustomException;
import com.binary.webide_be.ide.dto.*;
import com.binary.webide_be.ide.entity.FileData;
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

import static com.binary.webide_be.exception.message.ErrorMsg.*;
import static com.binary.webide_be.exception.message.ErrorMsg.PARENT_FILE_NOT_FOUND;
import static com.binary.webide_be.exception.message.SuccessMsg.CREATE_FOLDER_SUCCESS;
import static com.binary.webide_be.exception.message.SuccessMsg.UPDATE_FOLDER_PATH_SUCCESS;

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
    public ResponseDto<?> updateFileParent(Long fileId, UpdateParentRequestDto updateParentRequestDto, UserDetailsImpl userDetails) {
        // 유저가 해당 프로젝트에 속해 있는지 확인
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Project project = projectRepository.findById(updateParentRequestDto.getProjectId())
                .orElseThrow(() -> new CustomException(PROJECT_NOT_FOUND));

        UserTeam userTeam = userTeamRepository.findByUserAndTeam(user, project.getTeam())
                .orElseThrow(() -> new CustomException(USER_NOT_IN_PROJECT_TEAM));

        // 파일 정보 가져오기
        FileData fileData = fileDataRepository.findById(fileId)
                .orElseThrow(() -> new CustomException(FILE_NOT_FOUND));

        // 새로운 부모 파일의 Id가 null이 아닌 경우, 새로운 부모 파일을 검색
        if(updateParentRequestDto.getParentId() != null) {
            FileData newParent = fileDataRepository.findById(updateParentRequestDto.getParentId())
                    .orElseThrow(() -> new CustomException(PARENT_FILE_NOT_FOUND));

            // 새로운 부모 파일이 프로젝트에 속해있는지 확인
            if(!newParent.getProjectId().equals(project)) {
                throw new CustomException(INVALID_PARENT_PROJECT); //TODO: 꼭 필요한 로직일까요?
            }

            fileData.setParentId(newParent);

        } else {
            // 새로운 부모 파일의 Id가 null인 경우, 루트 디렉토리로 설정하려는 의도로 간주하고, parentId를 null로 설정
            fileData.setParentId(null);
        }

        fileDataRepository.save(fileData);


        // 새로운 부모 ID에 따라 하위 데이터 조회 및 반환
        List<FileTreeResponseDto> descendants;

        if(updateParentRequestDto.getParentId() == null) {
            // 루트 레벨의 아이템들 조회
            descendants = findAllDescendants(null, project);
        } else {
            // 새로운 부모의 파일 데이터
            FileData newParent = fileDataRepository.findById(updateParentRequestDto.getParentId())
                    .orElseThrow(() -> new CustomException(PARENT_FILE_NOT_FOUND));

            // 새로운 부모의 부모 파일 데이터 (즉, 조부모 파일 데이터) 조회
            if(newParent.getParentId() != null) {
                FileData grandParentFileData = fileDataRepository.findById(newParent.getParentId().getFileId())
                        .orElseThrow(() -> new CustomException(PARENT_FILE_NOT_FOUND));

                descendants = findAllDescendants(grandParentFileData, project);
            } else {
                // 만약 newParent의 부모가 null이면, newParent 자체가 루트 레벨이거나 최상위 레벨에 있다는 의미입니다.
                // 이 경우 newParent의 자손들을 조회합니다.
                descendants = findAllDescendants(newParent, project);
            }
        }

        UpdateParentResponseDto updateParentResponseDto = new UpdateParentResponseDto(project, descendants);

        return ResponseDto.builder()
                .statusCode(UPDATE_FOLDER_PATH_SUCCESS.getHttpStatus().value())
                .data(updateParentResponseDto)
                .build();
    }


    // 하위 모든 자손 탐색 메소드
    private List<FileTreeResponseDto> findAllDescendants(FileData parent, Project project) {
        List<FileTreeResponseDto> dtoList = new ArrayList<>();
        List<FileData> items = new ArrayList<>();

        if (parent == null) {
            // 루트 레벨 하위 파일 데이터 조회
            items = fileDataRepository.findAllByProjectId(project);
        } else {
            // 특정 부모의 하위 파일 데이터들 조회
            Queue<FileData> queue = new LinkedList<>();
            queue.add(parent);

            while (!queue.isEmpty()) {
                // 큐에서 현재 처리할 부모 데이터 꺼냄
                FileData current = queue.poll();

                // 현재 파일 또는 폴더의 직접적인 자식들을 조회
                List<FileData> children = fileDataRepository.findByParentId(current);

                // 조회된 자식들을 임시 리스트에 추가
                items.addAll(children);

                // 조회된 자식들을 큐에 추가, 이 자식들의 자손도 추후에 탐색
                queue.addAll(children);
            }
        }

        // 탐색을 통해 모인 모든 파일 및 폴더에 대해 DTO 객체를 생성하여 최종 결과 리스트에 추가
        for (FileData item : items) {
            dtoList.add(new FileTreeResponseDto(item));
        }

        return dtoList;
    }

    @Transactional
    public ResponseDto<?> deleteFolder(Long projectId, FileData fileData, UserDetailsImpl userDetails) {
        log.info("deleteFolder 메서드");
        return null;
    }

    @Transactional
    public ResponseDto<?> updateFolderName(Long projectId, FileData fileData, String newName, UserDetailsImpl userDetails) {
        log.info("updateFolderName 메서드");
        return null;
    }
}
