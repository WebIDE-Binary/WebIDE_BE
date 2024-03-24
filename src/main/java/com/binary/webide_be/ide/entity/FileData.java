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

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private FileData parentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 1)
    private FileTypeEnum fileType;

    @Column(nullable = false, length = 50)
    private String fileName;

    @Column(length = 100)
    private String fileS3Address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project projectId;

    // JPA 엔티티 모델 내에서 관계를 명시적으로 나타내기 위한 형식적인 용도
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
}