package com.binary.webide_be.ide.entity;


import com.binary.webide_be.ide.dto.CreateFileRequestDto;
import com.binary.webide_be.ide.dto.CreateFolderRequestDto;
import com.binary.webide_be.project.entity.Project;
import com.binary.webide_be.util.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FileData extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    // 현재 FileData 인스턴스가 어떤 FileData 인스턴스의 자식인지를 나타내는 부모 참조
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private FileData parentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 1)
    private FileTypeEnum fileType;

    @Column(nullable = false, length = 50)
    private String fileName;

    @Column
    private String fileS3Address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project projectId;

    // 현재 FileData 인스턴스가 부모인 경우, 그에 종속된 모든 자식 FileData 인스턴스들의 리스트를 반환
    @OneToMany(mappedBy = "parentId")
    private List<FileData> children;

    public FileData(CreateFileRequestDto createFileRequestDto, Project project, FileData parent) {
        this.projectId = project;
        this.parentId = parent;
        this.fileName = createFileRequestDto.getFileName();
        this.fileType = createFileRequestDto.getFileType();
    }

    public FileData(CreateFolderRequestDto createFolderRequestDto, Project project, FileData parent) {
        this.projectId = project;
        this.parentId = parent;
        this.fileName = createFolderRequestDto.getFileName();
        this.fileType = createFolderRequestDto.getFileType();
    }

    public void updateParent(FileData newParent) {
        this.parentId = newParent;
    }

    public void updateName(String newFileName) {
        this.fileName = newFileName;
    }

    public void updateS3Address(String s3FileUrl) {
        this.fileS3Address = s3FileUrl;
    }

}