package com.platform.project_managment.group;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;
@Data
public class GroupDto {
    private Integer id;
    @NotBlank
    private String name;

    @NotBlank
    private String sector;

    @NotBlank
    private String universityYear;

    private Integer Numberstudents;

    private List<@Email String> pendingStudentEmails;
}


