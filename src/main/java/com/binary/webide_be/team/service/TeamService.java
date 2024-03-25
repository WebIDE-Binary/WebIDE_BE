package com.binary.webide_be.team.service;

import com.binary.webide_be.chat.entity.ChatRoom;
import com.binary.webide_be.chat.repository.ChatRoomRepository;
import com.binary.webide_be.exception.CustomException;
import com.binary.webide_be.security.UserDetailsImpl;
import com.binary.webide_be.team.dto.CreateRequestDto;
import com.binary.webide_be.team.dto.ManageResponseDto;
import com.binary.webide_be.team.dto.ModifyRequestDto;
import com.binary.webide_be.team.dto.ModifyResponseDto;
import com.binary.webide_be.team.entity.Team;
import com.binary.webide_be.team.entity.TeamRoleEnum;
import com.binary.webide_be.team.entity.UserTeam;
import com.binary.webide_be.team.repository.TeamRepository;
import com.binary.webide_be.team.repository.UserTeamRepository;
import com.binary.webide_be.user.entity.User;
import com.binary.webide_be.user.repository.UserRepository;
import com.binary.webide_be.util.dto.ResponseDto;

import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
    public ResponseDto<?> createTeam(CreateRequestDto<?> createRequestDto, UserDetailsImpl userDetails) {

        //생성에 필요한 필드 : 팀 이름, 참가자, 채팅방, 채팅 메세지
        Long userId = userDetails.getUser().getUserId();
        String teamName = createRequestDto.getTeamName();
        List<Long> participant = createRequestDto.getParticipants();

        User findUser = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );

        int teamSize = participant.size() + 1;

        Team team = new Team(teamName, teamSize);
        teamRepository.save(team);
        for (Long participantId : participant) { // 각 팀원의 ID를 순회
            User user = userRepository.findById(participantId).orElseThrow(
                    () -> new CustomException(USER_NOT_FOUND)
            );

            UserTeam userTeam = new UserTeam(user, team, TeamRoleEnum.USER); //팀원권한
            userTeamRepository.save(userTeam);
        }
        //팀장 설정
        UserTeam teamLeader = new UserTeam(findUser, team, TeamRoleEnum.LEADER);
        userTeamRepository.save(teamLeader);

        //팀원과 팀장을 하나의 팀으로 묶어서 chatroom에 저장하기
        ChatRoom chatRoom = new ChatRoom(team);
        chatRoomRepository.save(chatRoom);

        return ResponseDto.builder()
                .statusCode(CREATE_TEAM_SUCCESS.getHttpStatus().value())
                .message(CREATE_TEAM_SUCCESS.getDetail())
                .build();

    }

    //팀 수정
    public ResponseDto<?> updateTeamMembers(Long teamId, @NotEmpty List<Long> newMemberIds, UserDetailsImpl userDetails, org.apache.el.stream.Stream memberIdsToRemove) {

        //유저 검증
        User findUser = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(USER_NOT_FOUND)
        );
        // 팀 찾기
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new CustomException(TEAM_NOT_FOUND));

        // 현재 인증된 사용자가 팀장인지 확인
        UserTeam leaderCheck = userTeamRepository.findByUserAndTeam(findUser, team)
                .orElseThrow(() -> new CustomException(USER_NOT_TEAMLEADER));

        // 기존 팀원 목록 가져오기
        List<Team> currentMembers = userTeamRepository.findByTeam(team);

        // 새로운 팀원 ID와 기존 팀원 ID를 비교하여 추가, 삭제 결정
        Set<Long> currentMemberIds = currentMembers.stream()
                .map(member -> userDetails.getUser().getUserId())
                .collect(Collectors.toSet());

        // 새로운 팀원 추가
        List<Long> newMembersToAdd = newMemberIds.stream().filter(id -> !currentMemberIds.contains(id)).toList();
        for (Long userId : newMembersToAdd) {
            userRepository.findById(userId).ifPresent(user -> {
                UserTeam newUserTeam = new UserTeam(user, team, TeamRoleEnum.USER);
                userTeamRepository.save(newUserTeam);
            });
        }

        // 기존 팀원 삭제
        userTeamRepository.delete((UserTeam) currentMemberIds);

        return ResponseDto.builder()
                .statusCode(MODIFY_TEAM_SUCCESS.getHttpStatus().value())
                .message(MODIFY_TEAM_SUCCESS.getDetail())
                .data(new ModifyResponseDto())
                .build();
    }

}

