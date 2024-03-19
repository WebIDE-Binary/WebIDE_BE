package com.binary.webide_be.project.service;

import com.binary.webide_be.exception.CustomException;
import com.binary.webide_be.exception.message.SuccessMsg;
import com.binary.webide_be.project.dto.CreatePorjectResponseDto;
import com.binary.webide_be.project.dto.CreateProjectRequestDto;
import com.binary.webide_be.project.entity.*;
import com.binary.webide_be.project.repository.ProjectRepository;
import com.binary.webide_be.project.repository.TeamRepository;
import com.binary.webide_be.project.repository.UserTeamRepository;
import com.binary.webide_be.security.UserDetailsImpl;
import com.binary.webide_be.team.entity.Team;
import com.binary.webide_be.team.entity.TeamRoleEnum;
import com.binary.webide_be.team.entity.UserTeam;
import com.binary.webide_be.user.entity.User;
import com.binary.webide_be.user.repository.UserRepository;
import com.binary.webide_be.util.dto.ResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.binary.webide_be.exception.message.ErrorMsg.USER_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final UserTeamRepository userTeamRepository;


    //프로젝트 생성
    public ResponseDto<?> createProject(CreateProjectRequestDto projectRequestDto, UserDetailsImpl userDetails) {

        //프로젝트 생성 서비스에 필요한 필드들 (필요한거 가져오기)
        Long userId = projectRequestDto.getUserId();
        String projectName = projectRequestDto.getProjectName();
        String projectDesc = projectRequestDto.getProjectDesc();
        ProjectLanguagesEnum languagesEnum = projectRequestDto.getProjectLanguagesEnum();

        String teamName = projectRequestDto.getTeamName();
        List<Long> participants = projectRequestDto.getParticipantIds(); //팀원들의 Id값 -> 이터레이터로 팀원 한명씩 id가져오는 식을 만들어야한다.

        //Tip: 프로젝트 생성하면서 팀도 만들어야함
        //프로젝트 생성하는 유저 찾기 (얘 팀장 만들어야함)
        User findUser = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );

        //팀 만들기 (findUser가 팀장이 되는) (유저팀 다대다 연결 엔티티가 필요함)
        int teamSize = participants.size() + 1; //팀 사이즈 = 팀원 + 팀장1명까지

        Team team = new Team(teamName, teamSize); // 팀 객체 생성
        teamRepository.save(team); //팀을 DB 에 저장하기
        for (Long participant : participants) {   //팀원 가져오기 (이터레이터로 id 가져와 한명씩)
            User user = userRepository.findById(participant).orElseThrow(
                    () -> new CustomException(USER_NOT_FOUND)
            );
            //팀원을 팀에 연결하기
            UserTeam userTeam = new UserTeam(user, team, TeamRoleEnum.USER);
            userTeamRepository.save(userTeam);
        }
        //팀장 설정
        UserTeam teamLeader = new UserTeam(findUser, team, TeamRoleEnum.LEADER);
        userTeamRepository.save(teamLeader);

        //만들어진 프로젝트 객체를 DB에 저장
        Project project = new Project(projectName, projectDesc, languagesEnum, team);
        projectRepository.save(project);

        return ResponseDto.builder()
                .statusCode(SuccessMsg.EMAIL_CHECK_SUCCESS.getHttpStatus().value()) //TODO: SuccessMsg -> 추후 static으로 변경할것
                .data(new CreatePorjectResponseDto(findUser, team, project))
                .build();


    }


}
