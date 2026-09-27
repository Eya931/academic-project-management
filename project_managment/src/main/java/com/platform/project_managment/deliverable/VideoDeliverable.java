package com.platform.project_managment.deliverable;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Data;

@Entity
@Data
@PrimaryKeyJoinColumn(name = "deliverable_id")
public class VideoDeliverable extends Deliverable {
    private String videoUrl; // Pour les liens externes (YouTube, Vimeo)
    private String filePath; // Pour les vidéos stockées localement
    private Integer duration; // Durée en secondes
    private String format; // mp4, avi, etc.


    public VideoDeliverable() {
    }


}