package com.binary.webide_be.project.service;

import com.binary.webide_be.exception.CustomException;
import com.binary.webide_be.exception.message.SuccessMsg;
import com.binary.webide_be.project.dto.CreatePorjectResponseDto;
import com.binary.webide_be.project.dto.CreateProjectRequestDto;
import com.binary.webide_be.project.dto.UpdateProjectRequestDto;
import com.binary.webide_be.project.dto.UpdateProjectResponseDto;
import com.binary.webide_be.project.entity.*;
import com.binary.webide_be.project.repository.ProjectRepository;
import com.binary.webide_be.team.repository.UserTeamRepository;
import com.binary.webide_be.security.UserDetailsImpl;
import com.binary.webide_be.team.entity.Team;
import com.binary.webide_be.team.entity.TeamRoleEnum;
import com.binary.webide_be.team.entity.UserTeam;
import com.binary.webide_be.team.repository.TeamRepository;
import com.binary.webide_be.user.entity.User;
import com.binary.webide_be.user.repository.UserRepository;
import com.binary.webide_be.util.dto.ResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.binary.webide_be.exception.message.ErrorMsg.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final UserTeamRepository userTeamRepository;


    //프로젝트 생성
    public ResponseDto<?> createProject(CreateProjectRequestDto createProjectRequestDto, UserDetailsImpl userDetails) {

        //1. 프로젝트 생성 서비스에 필요한 필드들 (필요한거 가져오기)
        Long userId = createProjectRequestDto.getUserId(); //TODO: 얜 안씀 나중에 지우기
        String projectName = createProjectRequestDto.getProjectName();
        String projectDesc = createProjectRequestDto.getProjectDesc();
        ProjectLanguagesEnum languagesEnum = createProjectRequestDto.getProjectLanguagesEnum();

        String teamName = createProjectRequestDto.getTeamName();
        List<Long> participants = createProjectRequestDto.getParticipantIds(); //팀원들의 Id값 -> 이터레이터로 팀원 한명씩 id가져오는 식을 만들어야한다.


        //2. 프로젝트 생성하는 유저 찾기 (얘 팀장 만들어야함)
        User findUser = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );

        //3. 팀 만들기 (findUser가 팀장이 되는) (유저팀 다대다 연결 엔티티가 필요함)
        int teamSize = participants.size() + 1; //팀 사이즈 = 팀원 + 팀장1명까지

        Team team = new Team(teamName, teamSize); // 팀 객체 생성
        teamRepository.save(team); //팀을 DB 에 저장하기
        for (Long participant : participants) {   //팀원 가져오기 (이터레이터로 id 가져와 한명씩)
            User user = userRepository.findById(participant).orElseThrow(
                    () -> new CustomException(USER_NOT_FOUND)
            );
            //4. 팀원을 팀에 연결하기
            UserTeam userTeam = new UserTeam(user, team, TeamRoleEnum.USER);
            userTeamRepository.save(userTeam);
        }
        //5. 팀장 설정
        UserTeam teamLeader = new UserTeam(findUser, team, TeamRoleEnum.LEADER);
        userTeamRepository.save(teamLeader);

        //6. 만들어진 프로젝트 객체를 DB에 저장
        Project project = new Project(projectName, projectDesc, languagesEnum, team);
        projectRepository.save(project);

        //7. 반환하기
        return ResponseDto.builder()
                .statusCode(SuccessMsg.CREATE_PROJECT_SUCCESS.getHttpStatus().value()) //TODO: SuccessMsg -> 추후 static으로 변경할것
                .data(new CreatePorjectResponseDto(findUser, team, project)) //응답 DTO 생성자로 팀장, 팀, 프로젝트 만든것들 넣어주기
                .build();
    }

    //프로젝트 수정
    public ResponseDto<?> updateProject(Long projectId, UpdateProjectRequestDto updateProjectRequestDto, UserDetailsImpl userDetails) {
        //1. 프로젝트 수정에 필요한 것들을 updateProjectRequestDto에 담아서 가져옴
        String projectName = updateProjectRequestDto.getProjectName();
        String projectDesc = updateProjectRequestDto.getProjectDesc();

        //2. 프로젝트를 repository에서 찾고
        Project findProject = projectRepository.findById(projectId).orElseThrow(
                () -> new CustomException(PROJECT_NOT_FOUND)
        );

        //3. 유저를 찾고, 유저가 프로젝트를 담당하는 팀에 속해있는지, + 프로젝트에 속해있다면 팀장인지 확인하기(권환확인)
        User findUser = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(//TODO: 왜 findByEmail인지 물어보기(userDetails) 관련
                () -> new CustomException(USER_NOT_FOUND)
        );
        UserTeam userTeam = userTeamRepository.findByUserAndTeam(findUser, findProject.getTeam()).orElseThrow(
                () -> new CustomException(YOU_ARE_NOT_A_MEMBER_OF_THE_PROJECT_TEAM_AND_THEREFORE_CANNOT_PERFORM_THIS_ACTION) //프로젝트에 속해있지 않으면 업데이트 권한 없음
        );
        if (userTeam.getTeamRoleEnum() != TeamRoleEnum.LEADER) {
            throw new CustomException(NO_AUTHORITY_TO_UPDATE_PROJECT); //프로젝트 리더가 아니어도 업데이트 권한 없음
        }

        //4. 찾은 프로젝트 수정, team과 프로그래밍 언어는 변경하지 않고 기존값을 그대로 사용
        findProject.update(projectId, projectName, projectDesc);

        //JPA 변경감지 기능으로 수정사항이 데이터베이스에 자동 반영됨

        //5. 응답 반환
        return ResponseDto.builder()
                .statusCode(SuccessMsg.UPDATE_PROJECT_SUCCESS.getHttpStatus().value())
                .data(new UpdateProjectResponseDto(findProject))
                .build();
    }

    //프로젝트 삭제
    public ResponseDto<?> deleteProject(Long projectId, UserDetailsImpl userDetails) {
        //1. 프로젝트 찾기
        Project findProject = projectRepository.findById(projectId).orElseThrow(
                () -> new CustomException(PROJECT_NOT_FOUND)
        );

        //2. 유저를 찾고 + 유저가 프로젝트를 담당하는 팀에 속해있는지 + 현재 사용자가(찾은유저) 프로젝트의 팀장인지 확인
        User findUser = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );
        UserTeam userTeam = userTeamRepository.findByUserAndTeam(findUser, findProject.getTeam()).orElseThrow(
                () -> new CustomException(YOU_ARE_NOT_A_MEMBER_OF_THE_PROJECT_TEAM_AND_THEREFORE_CANNOT_PERFORM_THIS_ACTION)
        );
        if (userTeam.getTeamRoleEnum() != TeamRoleEnum.LEADER) {
            throw new CustomException(NO_AUTHORITY_TO_DELETE_PROJECT);
        }

        //3. 찾은 프로젝트 삭제하기
        projectRepository.delete(findProject);

        //4. 반환하기
        return ResponseDto.builder()
                .statusCode(SuccessMsg.DELETE_PROJECT_SUCCESS.getHttpStatus().value())
                .message(SuccessMsg.DELETE_PROJECT_SUCCESS.getDetail())
                .build();
    }

    public ResponseDto<?> searchProjects(String searchWord, UserDetailsImpl userDetails) {
        return null;
    }

    //내 프로젝트 목록 조회

        //유저를 찾고

        //유저가 속한 프로젝트를 리스트로 찾고

        //반환해주기??





}
