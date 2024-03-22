package com.binary.webide_be.ide.service;

import com.binary.webide_be.exception.CustomException;
import com.binary.webide_be.ide.dto.CreateFileRequestDto;
import com.binary.webide_be.ide.dto.CreateFileResponseDto;
import com.binary.webide_be.ide.dto.FileTreeResponseDto;
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
import org.springframework.stereotype.Service;

import static com.binary.webide_be.exception.message.ErrorMsg.*;
import static com.binary.webide_be.exception.message.SuccessMsg.CREATE_FILE_SUCCESS;

@Service
@RequiredArgsConstructor
public class FileService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserTeamRepository userTeamRepository;
    private final FileDataRepository fileDataRepository;

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
}
