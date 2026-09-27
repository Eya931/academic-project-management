package com.platform.project_managment.project;


import lombok.Data;

@Data
public class ProjectRequestDto {
        private Integer Id;
        private String  title;
        private String  category;
        private String  description;
        private String  dueDate;
        private Integer maxStudents;
        private String  assignedTo;
        private String    string;


}
