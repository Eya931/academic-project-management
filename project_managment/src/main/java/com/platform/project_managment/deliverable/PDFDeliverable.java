package com.platform.project_managment.deliverable;

import jakarta.persistence.Entity;

@Entity
public class PDFDeliverable extends Deliverable {
    private String filePath;
}
