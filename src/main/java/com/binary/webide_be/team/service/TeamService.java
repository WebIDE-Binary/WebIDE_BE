package com.binary.webide_be.team.service;

import com.binary.webide_be.chat.entity.ChatRoom;
import com.binary.webide_be.chat.repository.ChatRoomRepository;
import com.binary.webide_be.exception.CustomException;
import com.binary.webide_be.exception.message.SuccessMsg;
import com.binary.webide_be.security.UserDetailsImpl;
import com.binary.webide_be.team.dto.CreateRequestDto;

import com.binary.webide_be.team.dto.FindTeamResponseDto;
import com.binary.webide_be.team.dto.ModifyRequestDto;
import com.binary.webide_be.team.entity.Team;
import com.binary.webide_be.team.entity.TeamRoleEnum;
import com.binary.webide_be.team.entity.UserTeam;
import com.binary.webide_be.team.repository.TeamRepository;
import com.binary.webide_be.team.repository.UserTeamRepository;
import com.binary.webide_be.user.entity.User;
import com.binary.webide_be.user.repository.UserRepository;
import com.binary.webide_be.util.dto.ResponseDto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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


   public ResponseDto<?> modifiedTeam(ModifyRequestDto modifyRequestDto, UserDetailsImpl userDetails) {

       //수정에 필요한 정보 ; userid(즉,팀장), 팀 이름, 팀원인 참가자
       Long userId = userDetails.getUser().getUserId(); // 만든사람 id

       //팀 조회
       Team team = teamRepository.findById(modifyRequestDto.getTeamId())
               .orElseThrow(() -> new CustomException(TEAM_NOT_FOUND));

       // 팀 이름 변경
       if (modifyRequestDto.getTeamName() != null) {
           team.setTeamName(modifyRequestDto.getTeamName());
       }

       // 참가자 변경 (적절한 변환 로직이 필요할 수 있습니다)
       if (modifyRequestDto.getParticipants() != null) {
           team.setParticipants(modifyRequestDto.getParticipants());
       }
       teamRepository.save(team); // 변경된 팀 정보 저장

       return ResponseDto.builder()
               .statusCode(MODIFY_TEAM_SUCCESS.getHttpStatus().value())
               .message(MODIFY_TEAM_SUCCESS.getDetail())
               .data(team)
               .build();
   }


//   public ResponseDto manageTeam(ManageRequestDto manageResponseDto, UserDetailsImpl userDetails) {
//
//        //팀목록 보여주기
////        public List<Team> getAllTeams() { return null;
////           List<Team> teams = teamRepository.findAllById(userDetails);
////           return teams.stream().map(this::createTeam).collect(Collectors.toList());
////       List<Long> teamIds = // userDetails로부터 팀 ID 목록을 얻는 로직; 요건 위에 있음
//
//            List<Team> teams = teamRepository.findAllById(Collections.singleton(team.getTeamId()));
//            List<ResponseDto<?>> list = new ArrayList<>();
//            for (Team team1 : teams) {
//                ResponseDto<?> responseDto = createTeam(team1);
//                list.add(responseDto); //이게 맞는건가> for로 돌려서 team1을 꺼낸다 -> 근데 그냥 나열인텐데.....????
//            }
//            return (ResponseDto<?>) list;
//
//
//        //채팅방 보여주기
//       public List<ChatMessageResponseDto> getChatRoomsByTeam(Long teamId) {
//            return ChatMessageRequestDto.stream()
//                    .map(chatRoom -> new ChatMessageResponseDto(//연결하고자 하는 필드)
//                            .collect(Collectors.toList());
//       }
//
//
//
//        //검색기능
//       public List<User> searchParticipants(String keyword) {
//           return userRepository.findByUsernameContainingOrEmailContaining(keyword, keyword);
//       }
//   }

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
}