package com.binary.webide_be.project.service;

import com.binary.webide_be.exception.CustomException;
import com.binary.webide_be.exception.message.SuccessMsg;
import com.binary.webide_be.project.dto.*;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        //1. 프로젝트 생성 서비스에 필요한 것들 requsestDTO 에서 가져옴
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

        //3. 팀 만들기 (findUser가 팀장이 되는) (유저팀 다대다 연결 엔티티가 필요함) //팀 사이즈 = 팀원 + 팀장1명까지
        int teamSize = participants.size() + 1;

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
        //1. 프로젝트 수정에 필요한 것들을 requsestDTO 에서 가져옴
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
        //useeTeam 테이블에서 유저가 어떤 팀에 속해있는지 찾기
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
        //1. 프로젝트 찾기 (프로젝트 아이디로 현재 프로젝트를 찾음)
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

    //내 프로젝트 목록 조회
    public ResponseDto<?> findProjects(String searchWord, UserDetailsImpl userDetails) { //들어오는 매개변수들 (검색단어, 유저권한있는지)
        //1. 유저찾기: UserDetailsImpl을 통해 현재 로그인한 유저의 정보를 얻어옴
        User findUser = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );

        //2.사용자가 속한 UserTeam테이블 조회
        List<UserTeam> findUserTeams = userTeamRepository.findByUser(findUser); //유저 정보로 userTeam테이블에 유저랑 관련된 userTeam 정보를 다 가져옴

        //사용자가 속한 팀만 뽑기 (응답에 팀 아이디, 팀 네임 들어가야함)
        List<Team> findTeams = findUserTeams.stream()
                .map(UserTeam::getTeam) //팀만 뽑아오기 -> 사용자가 속한 팀 정보만 가져옴
                .collect(Collectors.toList()); //그걸 리스트로 담아주기

        //샤용자가 속한 팀의 프로젝트들 조회하기 (내가속한 팀과 -> 팀에서 참여하는 프로젝트를 리스트로 받기(여러개니깐))
        List<Project> findProjects = projectRepository.findByTeamIn(findTeams);


        //유저가 속해있는 팀의 -> 유저들의 이미지를 가져와야한다.
        List<User> findUsers = findUserTeams.stream()
                .map(UserTeam::getUser) //유저만 뽑아오기 
                .collect(Collectors.toList());

        List<String> userImages = new ArrayList<>();
        for (User finduser : findUsers) {
            userImages.add(finduser.getProfileImg());//
        }


        //4. 프로젝트 정보를 응답DTO로 변환하여 응답 준비 (여기에 담기)
        List<FindProjectResponseDto> findProjectResponseDtos = findProjects.stream()
                .map(project -> new FindProjectResponseDto(project, userImages))
                .filter(project -> project.getProjectName().contains(searchWord) || project.getTeamName().contains(searchWord)) //단건으로 뽑은얘를 -> project로 이름짓겠다. + 프로젝트 이름, 팀 이름으로 검색기능
                .collect(Collectors.toList());

        //5. 반환하기
        return ResponseDto.builder()
                .statusCode(SuccessMsg.SEARCH_PROJECT_SUCCESS.getHttpStatus().value())
                .data(findProjectResponseDtos) //searchProjectResponseDtos에 필요한 정보가 다 담겨있다.
                .build();
    }


}
