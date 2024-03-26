package com.binary.webide_be.userSearch.service;

import com.binary.webide_be.exception.CustomException;
import com.binary.webide_be.security.UserDetailsImpl;
import com.binary.webide_be.user.entity.User;
import com.binary.webide_be.user.repository.UserRepository;
import com.binary.webide_be.userSearch.dto.UserSearchResponseDto;
import com.binary.webide_be.util.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.binary.webide_be.exception.message.ErrorMsg.INVALID_SEARCH_TERM;
import static com.binary.webide_be.exception.message.ErrorMsg.USER_NOT_FOUND;
import static com.binary.webide_be.exception.message.SuccessMsg.SEARCH_USER_SUCCESS;

@Service
@RequiredArgsConstructor
public class UserSearchService {
    private final UserRepository userRepository;

    public ResponseDto<?> searchUsersList(String searchWord, UserDetailsImpl userDetails) {
        // 문자열이 null, 빈 문자열(""), 또는 공백 문자로만 구성되었는지 체크
        if (StringUtils.isBlank(searchWord)) {
            throw new CustomException(INVALID_SEARCH_TERM);
        }

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

        List<UserSearchResponseDto> searchUsersList = userRepository.findByEmailContainingOrNickNameContaining(searchWord, searchWord)
                .stream()
                .map(UserSearchResponseDto::new)
                .collect(Collectors.toList());

        return ResponseDto.builder()
                .statusCode(SEARCH_USER_SUCCESS.getHttpStatus().value())
                .message(SEARCH_USER_SUCCESS.getDetail())
                .data(searchUsersList)
                .build();
    }
}
