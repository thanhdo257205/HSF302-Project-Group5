# Hướng dẫn chi tiết: Chức năng Duyệt tin tuyển dụng (Admin) - Sử dụng DTO Request/Response, Chuyển hướng trang và Form Action giống dự án Course

Tài liệu này hướng dẫn cách triển khai chức năng **Duyệt tin tuyển dụng** theo phong cách code chuẩn của bạn (sử dụng DTO, chuyển hướng trang chi tiết, bind Enum trực tiếp và hiển thị thông báo flash message qua `RedirectAttributes`).

---

## Luồng hoạt động chính:
1. **Trang danh sách (`/admin/job-approval`)**: Hiển thị bảng danh sách các tin tuyển dụng cần xét duyệt bằng cách sử dụng `Page<JobPostDashboardResponse>` DTO.
2. **Trang chi tiết (`/admin/job-approval/{id}`)**: Chuyển hướng đến một trang mới hiển thị toàn bộ nội dung của tin tuyển dụng bằng cách sử dụng trực tiếp thực thể `JobPost` để hiển thị đầy đủ chi tiết.
3. **Cập nhật trạng thái (`POST /admin/job-approval/edit/{id}`)**: Cập nhật trạng thái tin đăng (APPROVED hoặc REJECTED), sau đó chuyển hướng kèm theo thông báo thành công dạng flash message về chính trang chi tiết đó.

---

## Các bước triển khai:

### Bước 1: Cập nhật Repository Layer (`JobPostRepository`)

Mở file: `vn/edu/fpt/hsf302_group5/repository/jobpost/JobPostRepository.java`

Thực hiện truy vấn trả về thực thể **`Page<JobPost>`** (Entity) và dùng `LEFT JOIN FETCH` để nạp dữ liệu liên quan:

```java
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.fpt.hsf302_group5.entity.JobPost;
import vn.edu.fpt.hsf302_group5.entity.enums.JobStatus;

public interface JobPostRepository extends JpaRepository<JobPost, Integer> {
    
    // ... Các code cũ giữ nguyên ...

    // Truy vấn trả về Page<JobPost> Entity, nạp sẵn thông tin Recruiter và Company
    @Query("""
        SELECT j FROM JobPost j 
        LEFT JOIN FETCH j.recruiter r 
        LEFT JOIN FETCH r.company c 
        WHERE (:status IS NULL OR j.status = :status) 
          AND (:keyword IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :keyword, '%')) 
               OR LOWER(c.companyName) LIKE LOWER(CONCAT('%', :keyword, '%')))
    """)
    Page<JobPost> findAllForApproval(
            @Param("status") JobStatus status,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    // Đếm tổng số lượng tin theo trạng thái
    long countByStatus(JobStatus status);
}
```

---

### Bước 2: Cập nhật Service Layer (`AdminService` & `AdminServiceImpl`)

#### 1. Cập nhật Interface `AdminService.java`
Mở file: `vn/edu/fpt/hsf302_group5/service/user/AdminService.java`

```java
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.fpt.hsf302_group5.dto.admin.JobPostDashboardResponse;
import vn.edu.fpt.hsf302_group5.entity.JobPost;
import vn.edu.fpt.hsf302_group5.entity.enums.JobStatus;

public interface AdminService {
    // ... Các phương thức cũ giữ nguyên ...
    
    // Trả về DTO cho Controller
    Page<JobPostDashboardResponse> getJobPostsForApproval(String keyword, JobStatus status, Pageable pageable);
    
    long countJobPostsByStatus(JobStatus status);
    
    JobPost getJobPostById(Integer jobId);
    
    void updateJobPostStatus(Integer jobId, JobStatus status, String comment, String adminEmail);
}
```

#### 2. Cập nhật Class `AdminServiceImpl.java`
Mở file: `vn/edu/fpt/hsf302_group5/service/impl/user/AdminServiceImpl.java`

```java
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.fpt.hsf302_group5.dto.admin.JobPostDashboardResponse;
import vn.edu.fpt.hsf302_group5.entity.User;
import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService {

    // ... Các hàm cũ giữ nguyên ...

    @Override
    public Page<JobPostDashboardResponse> getJobPostsForApproval(String keyword, JobStatus status, Pageable pageable) {
        if (keyword != null && keyword.trim().isEmpty()) {
            keyword = null;
        }
        Page<JobPost> entityPage = jobPostRepository.findAllForApproval(status, keyword, pageable);
        
        // Map từ Entity sang DTO tại tầng Service
        return entityPage.map(job -> new JobPostDashboardResponse(
                job.getJobId(),
                job.getTitle(),
                job.getRecruiter().getCompany().getCompanyName(),
                job.getStatus(),
                job.getPostedDate()
        ));
    }

    @Override
    public long countJobPostsByStatus(JobStatus status) {
        return jobPostRepository.countByStatus(status);
    }

    @Override
    public JobPost getJobPostById(Integer jobId) {
        return jobPostRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tin tuyển dụng với ID: " + jobId));
    }

    @Override
    @Transactional
    public void updateJobPostStatus(Integer jobId, JobStatus status, String comment, String adminEmail) {
        JobPost jobPost = jobPostRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tin tuyển dụng với ID: " + jobId));
        
        User adminUser = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thông tin Admin đang xử lý!"));

        jobPost.setStatus(status);
        jobPost.setAdminComment(comment);
        jobPost.setApprovedBy(adminUser.getUserId());
        jobPost.setApprovedDate(LocalDateTime.now());
        
        jobPostRepository.save(jobPost);
    }
}
```

---

### Bước 3: Cập nhật Controller Layer (`AdminController`)

Mở file: `vn/edu/fpt/hsf302_group5/controller/admin/AdminController.java`

Triển khai API danh sách, chi tiết và API cập nhật trạng thái tin tuyển dụng theo phong cách của bạn (Sử dụng trực tiếp Enum `JobStatus`, sử dụng `RedirectAttributes` truyền flash message và chuyển hướng về trang chi tiết):

```java
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.fpt.hsf302_group5.dto.admin.JobPostDashboardResponse;
import vn.edu.fpt.hsf302_group5.entity.JobPost;
import vn.edu.fpt.hsf302_group5.entity.enums.JobStatus;
import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    
    public AdminController(AdminService adminService){
        this.adminService = adminService;
    }

    // 1. GET: Trang danh sách tin tuyển dụng (nhận Page DTO từ Service)
    @GetMapping("/job-approval")
    public String viewJobApproval(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) JobStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("postedDate").descending());
        
        Page<JobPostDashboardResponse> jobPage = adminService.getJobPostsForApproval(keyword, status, pageable);

        model.addAttribute("jobPage", jobPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("statusFilter", status);

        // Các thống kê hiển thị ở đầu trang
        model.addAttribute("pendingCount", adminService.countJobPostsByStatus(JobStatus.PENDING));
        model.addAttribute("approvedCount", adminService.countJobPostsByStatus(JobStatus.APPROVED));
        model.addAttribute("rejectedCount", adminService.countJobPostsByStatus(JobStatus.REJECTED));

        return "pages/admin/job-approval";
    }

    // 2. GET: Trang chi tiết phục vụ duyệt
    @GetMapping("/job-approval/{id}")
    public String viewJobDetailForApproval(@PathVariable("id") Integer id, Model model) {
        try {
            JobPost jobPost = adminService.getJobPostById(id);
            model.addAttribute("job", jobPost);
            return "pages/admin/job-detail-approval";
        } catch (Exception e) {
            return "redirect:/admin/job-approval?error=" + e.getMessage();
        }
    }

    // 3. POST: Cập nhật trạng thái tin tuyển dụng (Duyệt/Từ chối)
    // Style đồng bộ với dự án quản lý Course của bạn
    @PostMapping("/job-approval/edit/{id}")
    public String updateJobPostStatus(
            @PathVariable Integer id,
            @RequestParam("status") JobStatus status, // Nhận trực tiếp kiểu Enum cực kỳ an toàn
            @RequestParam(value = "adminComment", required = false) String adminComment,
            RedirectAttributes redirectAttributes,
            Principal principal
    ) {
        try {
            adminService.updateJobPostStatus(id, status, adminComment, principal.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật trạng thái tin tuyển dụng thành công.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cập nhật thất bại: " + e.getMessage());
        }

        // Redirect chuyển hướng về lại chính trang chi tiết đó
        return "redirect:/admin/job-approval/" + id;
    }
}
```

---

### Bước 4: Cập nhật Giao diện Thymeleaf

#### 1. Trang danh sách: `templates/pages/admin/job-approval.html`

```html
<!-- Bộ lọc tìm kiếm & Lọc trạng thái -->
<div class="card card-custom p-4">
    <div class="d-flex flex-wrap justify-content-between align-items-center gap-3 mb-4">
        <h5 class="fw-bold mb-0">Danh sách tin gửi duyệt</h5>
        <form th:action="@{/admin/job-approval}" method="get" class="d-flex gap-2 align-items-center">
            <select class="form-select" name="status" style="width: 160px;" onchange="this.form.submit()">
                <option value="" th:selected="${statusFilter == null}">Tất cả trạng thái</option>
                <option value="PENDING" th:selected="${statusFilter != null && statusFilter.name() == 'PENDING'}">Chờ duyệt</option>
                <option value="APPROVED" th:selected="${statusFilter != null && statusFilter.name() == 'APPROVED'}">Đã duyệt</option>
                <option value="REJECTED" th:selected="${statusFilter != null && statusFilter.name() == 'REJECTED'}">Bị từ chối</option>
            </select>
            <div style="width: 250px;">
                <input type="text" class="form-control" name="keyword" th:value="${keyword}" placeholder="Tìm kiếm tiêu đề...">
            </div>
            <button class="btn btn-primary" type="submit">Tìm kiếm</button>
        </form>
    </div>

<div class="table-responsive">
    <table class="table table-hover align-middle">
        <thead>
        <tr>
            <th>Công ty</th>
            <th>Tiêu đề công việc</th>
            <th>Ngày gửi</th>
            <th>Trạng thái</th>
            <th class="text-end">Thao tác</th>
        </tr>
        </thead>
        <tbody>
        <tr th:each="job : ${jobPage.content}">
            <td>
                <div class="fw-bold" th:text="${job.companyName}">TechCorp Vietnam</div>
            </td>
            <td th:text="${job.title}">Senior Frontend Developer</td>
            <td class="text-muted" th:text="${#temporals.format(job.postedDate, 'dd/MM/yyyy HH:mm')}">15/06/2026</td>
            <td>
                <span th:if="${job.status == 'PENDING'}" class="badge bg-warning text-dark">Chờ duyệt</span>
                <span th:if="${job.status == 'APPROVED'}" class="badge bg-success">Đã duyệt</span>
                <span th:if="${job.status == 'REJECTED'}" class="badge bg-danger">Từ chối</span>
                <span th:if="${job.status == 'CLOSED'}" class="badge bg-secondary">Đã đóng</span>
            </td>
            <td class="text-end">
                <a th:href="@{/admin/job-approval/{id}(id=${job.jobId})}" class="btn btn-sm btn-primary-custom">
                    Xem chi tiết
                </a>
            </td>
        </tr>
        <tr th:if="${#lists.isEmpty(jobPage.content)}">
            <td colspan="5" class="text-center py-4 text-muted">Không tìm thấy tin tuyển dụng nào phù hợp.</td>
        </tr>
        </tbody>
    </table>
</div>

<!-- Phân trang dưới bảng dữ liệu -->
<nav aria-label="Page navigation" class="mt-3" th:if="${jobPage.totalPages > 0}">
    <ul class="pagination justify-content-end mb-0">
        <li class="page-item" th:classappend="${jobPage.first ? 'disabled' : ''}">
            <a class="page-link" th:href="@{/admin/job-approval(page=${jobPage.number - 1}, keyword=${keyword}, status=${statusFilter})}">Trước</a>
        </li>
        <li class="page-item" th:each="i : ${#numbers.sequence(0, jobPage.totalPages - 1)}" th:classappend="${jobPage.number == i ? 'active' : ''}">
            <a class="page-link" th:href="@{/admin/job-approval(page=${i}, keyword=${keyword}, status=${statusFilter})}" th:text="${i + 1}">1</a>
        </li>
        <li class="page-item" th:classappend="${jobPage.last ? 'disabled' : ''}">
            <a class="page-link" th:href="@{/admin/job-approval(page=${jobPage.number + 1}, keyword=${keyword}, status=${statusFilter})}">Sau</a>
        </li>
    </ul>
</nav>
```

---

#### 2. Tạo trang chi tiết mới: `templates/pages/admin/job-detail-approval.html`

Trang này hiển thị thông tin công việc chi tiết. Không dùng Javascript, sử dụng 2 Form HTML riêng gửi trực tiếp lên API sửa đổi `/admin/job-approval/edit/{id}`:

```html
<!DOCTYPE html>
<html lang="vi" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết duyệt tin - JobPortal Admin</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">

    <link rel="stylesheet" th:href="@{/assets/css/common.css}">
    <link rel="stylesheet" th:href="@{/assets/css/admin.css}">
</head>
<body>

<div th:replace="~{fragments/header :: header}"></div>

<div class="admin-layout">
    <div th:replace="~{fragments/admin-sidebar :: admin-sidebar('job-approval')}"></div>

    <main class="admin-content">
        <!-- Nút quay lại -->
        <div class="mb-3">
            <a th:href="@{/admin/job-approval}" class="btn btn-sm btn-outline-secondary">
                <i class="bi bi-arrow-left"></i> Quay lại danh sách
            </a>
        </div>

        <!-- Thông báo Flash message từ RedirectAttributes -->
        <div th:if="${successMessage}" class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="bi bi-check-circle-fill me-2"></i> <span th:text="${successMessage}">Thành công</span>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
        <div th:if="${errorMessage}" class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="bi bi-exclamation-triangle-fill me-2"></i> <span th:text="${errorMessage}">Lỗi</span>
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>

        <div class="card card-custom p-4 shadow-sm mb-4">
            <!-- Thông tin đầu trang -->
            <div class="d-flex align-items-center gap-3 mb-4 border-bottom pb-3">
                <img th:src="${#strings.isEmpty(job.recruiter.company.logoUrl) ? 'https://ui-avatars.com/api/?name=' + job.recruiter.company.companyName + '&background=random&color=fff&size=60' : job.recruiter.company.logoUrl}"
                     class="rounded border" style="width: 60px; height: 60px; object-fit: cover;" alt="Logo">
                <div>
                    <h3 class="fw-bold text-primary mb-1" th:text="${job.title}">Senior Frontend Developer</h3>
                    <p class="text-muted mb-0 fs-5" th:text="${job.recruiter.company.companyName}">TechCorp Vietnam</p>
                </div>
            </div>

            <!-- Trạng thái hiện tại -->
            <div class="mb-4">
                <span class="fs-6 fw-bold me-2">Trạng thái:</span>
                <span th:if="${job.status.name() == 'PENDING'}" class="badge bg-warning text-dark fs-6">Chờ duyệt</span>
                <span th:if="${job.status.name() == 'APPROVED'}" class="badge bg-success fs-6">Đã duyệt</span>
                <span th:if="${job.status.name() == 'REJECTED'}" class="badge bg-danger fs-6">Từ chối</span>
                <span th:if="${job.status.name() == 'CLOSED'}" class="badge bg-secondary fs-6">Đã đóng</span>
            </div>

            <!-- Thông tin cơ bản -->
            <div class="row mb-4 bg-light p-3 rounded">
                <div class="col-md-6 mb-2">
                    <strong>Mức lương:</strong> 
                    <span th:text="${(job.salaryMin != null ? job.salaryMin + ' - ' : '') + (job.salaryMax != null ? job.salaryMax : 'Thỏa thuận')}">1000 - 2000 USD</span>
                </div>
                <div class="col-md-6 mb-2">
                    <strong>Địa điểm:</strong> 
                    <span th:text="${job.locationDetail + ', ' + (job.province != null ? job.province.provinceName : '')}">Hồ Chí Minh</span>
                </div>
                <div class="col-md-6 mb-2">
                    <strong>Kinh nghiệm:</strong> 
                    <span th:text="${job.experienceLevel != null ? job.experienceLevel.name() : 'Không yêu cầu'}">3 - 5 năm</span>
                </div>
                <div class="col-md-6 mb-2">
                    <strong>Loại hình:</strong> 
                    <span th:text="${job.employmentType != null ? job.employmentType.name() : 'Khác'}">Full-time</span>
                </div>
                <div class="col-md-6 mb-2">
                    <strong>Ngày gửi yêu cầu:</strong>
                    <span th:text="${#temporals.format(job.postedDate, 'dd/MM/yyyy HH:mm')}">15/06/2026</span>
                </div>
            </div>

            <!-- Nội dung bài đăng -->
            <div class="mb-4">
                <h5 class="fw-bold text-dark border-bottom pb-2">Mô tả công việc</h5>
                <div class="text-muted" style="white-space: pre-line;" th:text="${job.description}">Mô tả...</div>
            </div>

            <div class="mb-4">
                <h5 class="fw-bold text-dark border-bottom pb-2">Yêu cầu ứng viên</h5>
                <div class="text-muted" style="white-space: pre-line;" th:text="${job.requirement}">Yêu cầu...</div>
            </div>

            <div class="mb-4">
                <h5 class="fw-bold text-dark border-bottom pb-2">Quyền lợi được hưởng</h5>
                <div class="text-muted" style="white-space: pre-line;" th:text="${job.benefit}">Quyền lợi...</div>
            </div>

            <!-- Nếu tin đã bị từ chối trước đó, hiển thị lý do từ chối -->
            <div class="mb-4 p-3 border border-danger bg-light rounded" th:if="${job.status.name() == 'REJECTED' && job.adminComment != null}">
                <h6 class="fw-bold text-danger"><i class="bi bi-exclamation-triangle-fill"></i> Lý do bị từ chối từ Admin:</h6>
                <p class="text-muted mb-0" th:text="${job.adminComment}">Lý do từ chối...</p>
            </div>
            
            <!-- Nếu tin đã được duyệt, hiển thị bình luận duyệt (nếu có) -->
            <div class="mb-4 p-3 border border-success bg-light rounded" th:if="${job.status.name() == 'APPROVED' && job.adminComment != null && !#strings.isEmpty(job.adminComment)}">
                <h6 class="fw-bold text-success"><i class="bi bi-check-circle-fill"></i> Ghi chú duyệt:</h6>
                <p class="text-muted mb-0" th:text="${job.adminComment}">Ghi chú...</p>
            </div>

            <!-- PHẦN XỬ LÝ DUYỆT TIN (Chỉ hiển thị khi trạng thái là PENDING) -->
            <div th:if="${job.status.name() == 'PENDING'}" class="border-top pt-4">
                <h5 class="fw-bold text-dark mb-3">Xử lý phê duyệt tin</h5>
                
                <div class="d-flex flex-column gap-4">
                    <!-- FORM 1: PHÊ DUYỆT (Chuyển đổi URL khớp edit/{id}) -->
                    <form th:action="@{/admin/job-approval/edit/{id}(id=${job.jobId})}" method="post">
                        <!-- Gửi thẳng JobStatus APPROVED kiểu Enum -->
                        <input type="hidden" name="status" value="APPROVED" />
                        <button type="submit" class="btn btn-success px-4 py-2">
                            <i class="bi bi-check-lg"></i> Đồng ý phê duyệt tin đăng
                        </button>
                    </form>
                    
                    <hr>

                    <!-- FORM 2: TỪ CHỐI (Gửi thẳng JobStatus REJECTED kiểu Enum kèm lý do bắt buộc) -->
                    <form th:action="@{/admin/job-approval/edit/{id}(id=${job.jobId})}" method="post" class="card p-3 bg-light border-danger">
                        <input type="hidden" name="status" value="REJECTED" />
                        
                        <div class="mb-3">
                            <label for="adminComment" class="form-label fw-bold text-danger">Lý do từ chối *</label>
                            <textarea class="form-control" name="adminComment" id="adminComment" rows="3" required placeholder="Nhập lý do từ chối tin tại đây (Bắt buộc)..."></textarea>
                        </div>
                        
                        <div>
                            <button type="submit" class="btn btn-danger px-4 py-2">
                                <i class="bi bi-x-lg"></i> Từ chối tin tuyển dụng
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </main>
</div>

<div th:replace="~{fragments/footer :: footer}"></div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
```
