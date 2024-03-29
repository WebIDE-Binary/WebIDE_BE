package com.binary.webide_be.team.service;

import com.binary.webide_be.chat.entity.ChatRoom;
import com.binary.webide_be.chat.repository.ChatRoomRepository;
import com.binary.webide_be.exception.CustomException;
import com.binary.webide_be.exception.message.SuccessMsg;
import com.binary.webide_be.security.UserDetailsImpl;
import com.binary.webide_be.team.dto.*;

import com.binary.webide_be.team.entity.Team;
import com.binary.webide_be.team.entity.TeamRoleEnum;
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
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;


import static com.binary.webide_be.exception.message.ErrorMsg.*;
import static com.binary.webide_be.exception.message.SuccessMsg.CREATE_TEAM_SUCCESS;
import static com.binary.webide_be.exception.message.SuccessMsg.MODIFY_TEAM_SUCCESS;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserTeamRepository userTeamRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    //팀 생성
    public ResponseDto<?> createTeam(CreateTeamRequestDto createTeamRequestDto, UserDetailsImpl userDetails) {
        //생성에 필요한 필드 : 팀 이름, 참가자, 채팅방
        Long userId = userDetails.getUser().getUserId();
        String teamName = createTeamRequestDto.getTeamName();
        List<Long> participant = createTeamRequestDto.getParticipants();

        User findUser = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );

        int teamSize = participant.size() + 1;

        // 팀 생성
        Team team = new Team(teamName, teamSize);
        Team creatTeam = teamRepository.save(team);

        // 유저팀 저장 - 팀원
        for (Long participantId : participant) { // 각 팀원의 ID를 순회
            User user = userRepository.findById(participantId).orElseThrow(
                    () -> new CustomException(USER_NOT_FOUND)
            );

            UserTeam userTeam = new UserTeam(user, team, TeamRoleEnum.USER); //팀원권한
            userTeamRepository.save(userTeam);
        }

        // 유저팀 저장 - 팀장
        UserTeam teamLeader = new UserTeam(findUser, team, TeamRoleEnum.LEADER);
        userTeamRepository.save(teamLeader);

        // 생성된 팀의 채팅방 생성
        ChatRoom chatRoom = new ChatRoom(creatTeam);
        chatRoomRepository.save(chatRoom);

        return ResponseDto.builder()
                .statusCode(CREATE_TEAM_SUCCESS.getHttpStatus().value())
                .message(CREATE_TEAM_SUCCESS.getDetail())
                .build();
    }

    //팀 수정
    @Transactional
    public ResponseDto<?> updateTeamMembers(Long teamId, ModifyTeamRequestDto modifyTeamRequestDto, UserDetailsImpl userDetails) {
        //유저 검증
        User findUser = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND));

        // 팀 찾기
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(TEAM_NOT_FOUND));

        // 현재 인증된 사용자의 유저팀 정보 불러오기
        UserTeam leaderCheck = userTeamRepository.findByUserAndTeam(findUser, team)
                .orElseThrow(() -> new CustomException(USER_NOT_IN_TEAM_PARTICIPANT));

        // 요청한 유저가 팀 리더인지 확인
        if(leaderCheck.getTeamRoleEnum() != TeamRoleEnum.LEADER) {
            throw new CustomException(USER_NOT_TEAMLEADER);
        }

        // 기존 팀원 목록 가져오기
        Set<User> existingUsers = userTeamRepository.findByTeam(team).stream()
                .map(UserTeam::getUser)
                .collect(Collectors.toSet());

        // 프론트엔드에서 전달된 팀원 목록을 Set으로 변환
        List<Long> userIds = modifyTeamRequestDto.getParticipants();
        Set<User> updatedUsers = userIds.stream()
                .map(userId -> userRepository.findById(userId)
                        .orElseThrow(() -> new CustomException(USER_NOT_FOUND)))
                .collect(Collectors.toSet());

        // 새로 추가될 팀원 찾기
        Set<User> usersToAdd = updatedUsers.stream()
                .filter(user -> !existingUsers.contains(user))
                .collect(Collectors.toSet());

        // 리더를 제외하고 삭제해야 할 사용자 찾기
        Set<User> usersToRemove = existingUsers.stream()
                .filter(user -> !updatedUsers.contains(user) && !user.equals(leaderCheck.getUser()))
                .collect(Collectors.toSet());

        // 새로운 팀원을 UserTeam에 추가
        usersToAdd.forEach(user -> userTeamRepository.save(new UserTeam(user, team, TeamRoleEnum.USER)));

        // 삭제할 팀원을 UserTeam에서 삭제
        userTeamRepository.findByTeam(team).stream()
                .filter(userTeam -> usersToRemove.contains(userTeam.getUser()))
                .forEach(userTeamRepository::delete);

        // 최종적으로 업데이트된 팀원 목록 조회
        List<UserTeam> finalUserTeams = userTeamRepository.findByTeam(team);

        // 팀 이름이 변경되었을 경우 팀이름 업데이트
        if (!team.getTeamName().equals(modifyTeamRequestDto.getTeamName())) {
            team.updateTeamName(modifyTeamRequestDto.getTeamName());
        }

        // 팀 사이즈 업데이트
        team.updateTeamSize(finalUserTeams.size());

        // 팀이름, 팀인원 변경 사항 저장
        teamRepository.save(team);

        // UserTeam 목록을 UserInfoDto 목록으로 변환
        List<UserInfoDto> finalParticipants = finalUserTeams.stream()
                .map(userTeam -> new UserInfoDto(userTeam.getUser()))
                .collect(Collectors.toList());

        return ResponseDto.builder()
                .statusCode(MODIFY_TEAM_SUCCESS.getHttpStatus().value())
                .message(MODIFY_TEAM_SUCCESS.getDetail())
                .data(new TeamInfoResponseDto(team.getTeamId(), team.getTeamName(), finalParticipants)) //여기 만들어야함
                .build();
    }


    //팀 목록 조회 서비스
    public ResponseDto<?> findTeam(UserDetailsImpl userDetails) { //들어오는 요청정보= 권한있는지
       //1. 유저 찾기
        User findUser = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );

        //2. 사용자가 어느팀에 있는지 알기 위해선 UserTeam 테이블에서 찾아야함 (유저와 팀 정보가 담겨있음)
        List<UserTeam> findUserTeam = userTeamRepository.findByUser(findUser);

        //3. 팀Id, 팀 이름, 채채팅방Id를 담을 응답 DTO를 만듬 (여러 채팅방에 필요하니깐)
        List<FindTeamResponseDto> findTeamResponseDtoList = new ArrayList<>();

        //4. for문으로 -> 사용자가 있는 findUserTeam정보로 사용자의 팀들과, 팀과연결된 채팅룸들을 가져옴
        for (UserTeam userTeam : findUserTeam) {
            Team team = teamRepository.findById(userTeam.getTeam().getTeamId()).orElseThrow(
                    () -> new CustomException(TEAM_NOT_FOUND)
            );

            ChatRoom chatRoom = chatRoomRepository.findByTeamId(team).orElseThrow(
                    () -> new CustomException(CHATROOM_NOT_FOUND)
            );

            //5. 응답 DTO 객체를 하나 만들어서 생성자로 필요한 팀Id, 팀이름, 팀과연결된 채팅방Id정보 담은 객체를 만듬
            FindTeamResponseDto findTeamResponseDto = new FindTeamResponseDto(team.getTeamId(), team.getTeamName(), chatRoom.getChatRoomId());
            findTeamResponseDtoList.add(findTeamResponseDto); //6. 아까 만들어놨던 응답 DTO 리스트에 추가해줌
        }

        return ResponseDto.builder()
                .statusCode(SuccessMsg.SEARCH_TEAM_SUCCESS.getHttpStatus().value())
                .data(findTeamResponseDtoList)
                .build();
    }

    //팀 정보 조회 서비스
    public ResponseDto<?> getTeamInfo(Long teamId, UserDetailsImpl userDetails) {
        User findUser = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(TEAM_NOT_FOUND));

        // 유저가 팀에 속해있는지 체크
        UserTeam userTeamCheck = userTeamRepository.findByUserAndTeam(findUser, team)
                .orElseThrow(() -> new CustomException(USER_NOT_IN_TEAM_PARTICIPANT));

        // 팀 정보 수정을 위한 조회 임으로 요청을 보낸 유저가 팀리터인지 확인
        if(userTeamCheck.getTeamRoleEnum() != TeamRoleEnum.LEADER) {
            throw new CustomException(USER_NOT_TEAMLEADER);
        }

        // 팀원 목록 조회
        List<UserTeam> userTeams = userTeamRepository.findByTeam(team);

        // UserTeam 목록을 UserInfoDto 목록으로 변환
        List<UserInfoDto> userInfoDtos = userTeams.stream()
                .map(userTeam -> new UserInfoDto(userTeam.getUser()))
                .collect(Collectors.toList());

        return ResponseDto.builder()
                .statusCode(SuccessMsg.SEARCH_TEAM_SUCCESS.getHttpStatus().value())
                .data(new TeamInfoResponseDto(team.getTeamId(), team.getTeamName(), userInfoDtos))
                .build();
    }
}
