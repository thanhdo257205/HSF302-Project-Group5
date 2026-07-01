package vn.edu.fpt.hsf302_group5.entity.enums;

public enum JobStatus {
    PENDING("Chờ duyệt"),
    APPROVED("Đang tuyển"),
    REJECTED("Bị từ chối"),
    CLOSED("Đã đóng");

    private final String displayName;

    JobStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}
