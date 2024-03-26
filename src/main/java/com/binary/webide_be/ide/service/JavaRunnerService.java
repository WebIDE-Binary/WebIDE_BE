package com.binary.webide_be.ide.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.binary.webide_be.exception.CustomException;
import com.binary.webide_be.exception.message.ErrorMsg;
import com.binary.webide_be.ide.dto.ExecutionRequestDto;
import com.binary.webide_be.ide.entity.FileData;
import com.binary.webide_be.ide.entity.FileTypeEnum;
import com.binary.webide_be.ide.repository.FileDataRepository;
import com.binary.webide_be.project.entity.Project;
import com.binary.webide_be.project.repository.ProjectRepository;
import com.binary.webide_be.security.UserDetailsImpl;
import com.binary.webide_be.team.entity.UserTeam;
import com.binary.webide_be.team.repository.UserTeamRepository;
import com.binary.webide_be.user.entity.User;
import com.binary.webide_be.user.repository.UserRepository;
import com.binary.webide_be.util.S3Service;
import com.binary.webide_be.util.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import javax.tools.*;
import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import static com.binary.webide_be.exception.message.ErrorMsg.FAILED_TO_COMPILE_FILE;
import static com.binary.webide_be.exception.message.ErrorMsg.FAILED_TO_EXECUTE_FILE;
import static com.binary.webide_be.exception.message.SuccessMsg.RUN_FILE_SUCCESS;

@Slf4j
@Service
@RequiredArgsConstructor
public class JavaRunnerService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserTeamRepository userTeamRepository;
    private final FileDataRepository fileDataRepository;

    public ResponseDto<?> executeJavaFile(String bucketName, Long fileId, ExecutionRequestDto executionRequestDto, UserDetailsImpl userDetails) {
        try {

            log.info("Executing Java file for fileId: {}", fileId);

            User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                    () -> new CustomException(ErrorMsg.USER_NOT_FOUND)
            );

            String fileS3Address = executionRequestDto.getFileS3Address();
            Long projectId = executionRequestDto.getProjectId();

            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new CustomException(ErrorMsg.PROJECT_NOT_FOUND));

            UserTeam userTeam = userTeamRepository.findByUserAndTeam(user, project.getTeam())
                    .orElseThrow(() -> new CustomException(ErrorMsg.USER_NOT_IN_PROJECT_TEAM));

            FileData fileData = fileDataRepository.findById(fileId).orElseThrow(
                    () -> new CustomException(ErrorMsg.FILE_NOT_FOUND)
            );

            if (fileData.getFileType() != FileTypeEnum.F) {
                throw new CustomException(ErrorMsg.THIS_TYPE_IS_NOT_FILE);
            }

            // S3 클라이언트 생성 시 리전 명시적으로 설정
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(Regions.AP_NORTHEAST_2)
                    .build();

            log.info("Downloading Java file from S3: {}", fileS3Address);

            // S3에서 Java 파일 다운로드
            S3Object object = s3Client.getObject(new GetObjectRequest(bucketName, fileS3Address));
            InputStream inputStream = object.getObjectContent();
            File javaFile = new File("temp.java");
            FileUtils.copyInputStreamToFile(inputStream, javaFile);

            log.info("Compiling and executing Java file");

            // Java 파일 컴파일 및 실행
            JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
            try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
                Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjects(javaFile);
                JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, null, null, null, compilationUnits);
                if (!task.call()) {
                    throw new CustomException(FAILED_TO_COMPILE_FILE);
                }
            } catch (IOException e) {
                throw new CustomException(FAILED_TO_COMPILE_FILE);
            }

            // Java 클래스 로드 및 실행
            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{javaFile.getParentFile().toURI().toURL()});
            Class<?> loadedClass = Class.forName("temp", true, classLoader);
            Method method = loadedClass.getDeclaredMethod("main", String[].class);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(outputStream);
            PrintStream oldOut = System.out;
            System.setOut(printStream);
            Object[] params = new Object[]{new String[]{}};
            method.invoke(null, params);
            System.out.flush();
            System.setOut(oldOut);
            String executionResult = outputStream.toString();

            // 결과 반환
            return ResponseDto.builder()
                    .statusCode(RUN_FILE_SUCCESS.getHttpStatus().value())
                    .message(RUN_FILE_SUCCESS.getDetail())
                    .data(executionResult)
                    .build();
        } catch (Exception e) {
            log.info("Failed to execute Java file for fileId: {}", fileId, e);
            throw new CustomException(FAILED_TO_EXECUTE_FILE);
        }
    }
}

