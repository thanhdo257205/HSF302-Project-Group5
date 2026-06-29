-- Nếu DB đã tồn tại thì xóa
USE master;
IF EXISTS (
    SELECT *
    FROM sys.databases
    WHERE name = 'hsf_group_project'
)
BEGIN
    ALTER DATABASE [hsf_group_project]
    SET SINGLE_USER
    WITH ROLLBACK IMMEDIATE;

    DROP DATABASE [hsf_group_project];
END
GO

-- Tạo mới Database
CREATE DATABASE [hsf_group_project];
GO

-- Sử dụng Database
USE [hsf_group_project];
GO

-- =========================
-- PROVINCES
-- =========================
CREATE TABLE provinces
(
    province_id   INT IDENTITY(1,1) PRIMARY KEY,
    province_code VARCHAR(20) NOT NULL UNIQUE,
    province_name NVARCHAR(100) NOT NULL UNIQUE
);

-- =========================
-- ADMINISTRATIVE UNITS
-- =========================
CREATE TABLE administrative_units
(
    unit_id      INT IDENTITY(1,1) PRIMARY KEY,
    province_id  INT NOT NULL,
    unit_code    VARCHAR(20) NOT NULL UNIQUE,
    unit_name    NVARCHAR(100) NOT NULL,
    unit_level   VARCHAR(20) NOT NULL
        CONSTRAINT ck_administrative_unit_level CHECK (unit_level IN ('COMMUNE', 'WARD', 'SPECIAL_ZONE')),

    CONSTRAINT fk_administrative_unit_province FOREIGN KEY (province_id)
        REFERENCES provinces (province_id),
    CONSTRAINT uq_administrative_unit_name UNIQUE (province_id, unit_name)
);

-- =========================
-- ROLES
-- =========================
CREATE TABLE roles
(
    role_id     INT IDENTITY(1,1) PRIMARY KEY,
    role_name   VARCHAR(50) NOT NULL UNIQUE,
    description NVARCHAR(255) NULL
);

-- =========================
-- PERMISSIONS
-- =========================
CREATE TABLE permissions
(
    permission_id   INT IDENTITY(1,1) PRIMARY KEY,
    permission_code VARCHAR(100) NOT NULL UNIQUE,
    permission_name NVARCHAR(150) NOT NULL,
    description     NVARCHAR(255) NULL
);

-- =========================
-- ROLE PERMISSIONS
-- =========================
CREATE TABLE role_permissions
(
    role_id       INT NOT NULL,
    permission_id INT NOT NULL,

    CONSTRAINT pk_role_permission PRIMARY KEY (role_id, permission_id),
    CONSTRAINT fk_role_permission_role FOREIGN KEY (role_id)
        REFERENCES roles (role_id) ON DELETE CASCADE,
    CONSTRAINT fk_role_permission_permission FOREIGN KEY (permission_id)
        REFERENCES permissions (permission_id) ON DELETE CASCADE
);

-- =========================
-- USERS
-- =========================
CREATE TABLE users
(
    user_id       INT IDENTITY(1,1) PRIMARY KEY,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name     NVARCHAR(100) NOT NULL,
    phone         VARCHAR(20),
    gender        VARCHAR(20) NULL,
    avatar_url    NVARCHAR(500),
    role_id       INT          NOT NULL,
    status        VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE'
        CONSTRAINT ck_users_status CHECK (status IN ('ACTIVE', 'INACTIVE')),
    created_at    DATETIME2    DEFAULT GETDATE(),
    updated_at    DATETIME2 NULL,

    CONSTRAINT fk_users_role FOREIGN KEY (role_id)
        REFERENCES roles (role_id)
);

-- =========================
-- COMPANIES
-- =========================
CREATE TABLE companies
(
    company_id                INT IDENTITY(1,1) PRIMARY KEY,
    company_name              NVARCHAR(200) NOT NULL,
    logo_url                  NVARCHAR(500),
    website                   VARCHAR(255),
    description               NVARCHAR(MAX),
    address_detail            NVARCHAR(255),
    province_id               INT NULL,
    administrative_unit_id    INT NULL,
    status                    VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
        CONSTRAINT ck_company_status CHECK (status IN ('ACTIVE', 'INACTIVE')),
    created_at                DATETIME2   DEFAULT GETDATE(),
    updated_at                DATETIME2 NULL,

    CONSTRAINT fk_company_province FOREIGN KEY (province_id)
        REFERENCES provinces (province_id),
    CONSTRAINT fk_company_administrative_unit FOREIGN KEY (administrative_unit_id)
        REFERENCES administrative_units (unit_id)
);

-- =========================
-- RECRUITERS
-- =========================
CREATE TABLE recruiters
(
    recruiter_id INT PRIMARY KEY,
    company_id   INT NOT NULL UNIQUE,

    CONSTRAINT fk_recruiter_user FOREIGN KEY (recruiter_id)
        REFERENCES users (user_id),
    CONSTRAINT fk_recruiter_company FOREIGN KEY (company_id)
        REFERENCES companies (company_id)
);

-- =========================
-- INDUSTRIES
-- =========================
CREATE TABLE industries
(
    industry_id   INT IDENTITY(1,1) PRIMARY KEY,
    industry_name NVARCHAR(100) NOT NULL UNIQUE
);

-- =========================
-- COMPANY INDUSTRIES
-- =========================
CREATE TABLE company_industries
(
    company_id  INT NOT NULL,
    industry_id INT NOT NULL,

    CONSTRAINT pk_company_industry PRIMARY KEY (company_id, industry_id),

    CONSTRAINT fk_company_industry_company FOREIGN KEY (company_id)
        REFERENCES companies (company_id),
    CONSTRAINT fk_company_industry_industry FOREIGN KEY (industry_id)
        REFERENCES industries (industry_id)
);

-- =========================
-- CANDIDATE PROFILES
-- =========================
CREATE TABLE candidate_profiles
(
    candidate_id             INT PRIMARY KEY,
    date_of_birth            DATE,
    gender                   VARCHAR(10)
        CONSTRAINT ck_candidate_gender CHECK (gender IN ('MALE', 'FEMALE', 'OTHER')),
    address_detail           NVARCHAR(255),
    province_id              INT NULL,
    administrative_unit_id   INT NULL,
    summary                  NVARCHAR(MAX),

    CONSTRAINT fk_candidate_user FOREIGN KEY (candidate_id)
        REFERENCES users (user_id),
    CONSTRAINT fk_candidate_province FOREIGN KEY (province_id)
        REFERENCES provinces (province_id),
    CONSTRAINT fk_candidate_administrative_unit FOREIGN KEY (administrative_unit_id)
        REFERENCES administrative_units (unit_id)
);

-- =========================
-- SKILLS
-- =========================
CREATE TABLE skills
(
    skill_id   INT IDENTITY(1,1) PRIMARY KEY,
    skill_name NVARCHAR(100) NOT NULL UNIQUE
);

-- =========================
-- CANDIDATE SKILLS
-- =========================
CREATE TABLE candidate_skills
(
    candidate_id INT NOT NULL,
    skill_id     INT NOT NULL,

    CONSTRAINT pk_candidate_skill PRIMARY KEY (candidate_id, skill_id),

    CONSTRAINT fk_candidate_skill_candidate FOREIGN KEY (candidate_id)
        REFERENCES candidate_profiles (candidate_id),
    CONSTRAINT fk_candidate_skill_skill FOREIGN KEY (skill_id)
        REFERENCES skills (skill_id)
);

-- =========================
-- EDUCATIONS
-- =========================
CREATE TABLE educations
(
    education_id INT IDENTITY(1,1) PRIMARY KEY,
    candidate_id INT NOT NULL,
    school_name  NVARCHAR(200) NOT NULL,
    degree       NVARCHAR(100),
    major        NVARCHAR(100),
    start_date   DATE,
    end_date     DATE,

    CONSTRAINT fk_education_candidate FOREIGN KEY (candidate_id)
        REFERENCES candidate_profiles (candidate_id)
);

-- =========================
-- EXPERIENCES
-- =========================
CREATE TABLE experiences
(
    experience_id INT IDENTITY(1,1) PRIMARY KEY,
    candidate_id  INT NOT NULL,
    company_name  NVARCHAR(200),
    position      NVARCHAR(100),
    description   NVARCHAR(MAX),
    start_date    DATE,
    end_date      DATE,

    CONSTRAINT fk_experience_candidate FOREIGN KEY (candidate_id)
        REFERENCES candidate_profiles (candidate_id)
);

-- =========================
-- CVS
-- =========================
CREATE TABLE cvs
(
    cv_id        INT IDENTITY(1,1) PRIMARY KEY,
    candidate_id INT          NOT NULL,
    cv_name      NVARCHAR(100) NOT NULL,
    file_name    NVARCHAR(255) NOT NULL,
    file_url     VARCHAR(500)  NOT NULL,
    uploaded_at  DATETIME2     DEFAULT GETDATE(),

    CONSTRAINT fk_cv_candidate FOREIGN KEY (candidate_id)
        REFERENCES candidate_profiles (candidate_id)
);

-- =========================
-- JOB POSTS
-- =========================
CREATE TABLE job_posts
(
    job_id                  INT IDENTITY(1,1) PRIMARY KEY,
    recruiter_id            INT           NOT NULL,
    industry_id             INT           NOT NULL,
    job_level               VARCHAR(30),
    vacancies               INT           DEFAULT 1,
    title                   NVARCHAR(200) NOT NULL,
    benefit                 NVARCHAR(MAX),
    experience_level         NVARCHAR(30),
    description             NVARCHAR(MAX) NOT NULL,
    requirement             NVARCHAR(MAX),
    location_detail         NVARCHAR(200),
    province_id             INT NULL,
    administrative_unit_id  INT NULL,
    salary_min              DECIMAL(18, 2),
    salary_max              DECIMAL(18, 2),
    employment_type         VARCHAR(30),
    status                  VARCHAR(30)   NOT NULL DEFAULT 'PENDING'
        CONSTRAINT ck_job_post_status CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED', 'CLOSED')),
    posted_date             DATETIME2     DEFAULT GETDATE(),
    expired_date            DATETIME2 NULL,
    approved_by             INT NULL,
    approved_date           DATETIME2 NULL,
    admin_comment           NVARCHAR(500),

    CONSTRAINT ck_salary CHECK (
        salary_min IS NULL
            OR salary_max IS NULL
            OR salary_min <= salary_max
        ),

    CONSTRAINT fk_job_post_recruiter FOREIGN KEY (recruiter_id)
        REFERENCES recruiters (recruiter_id),
    CONSTRAINT fk_job_post_industry FOREIGN KEY (industry_id)
        REFERENCES industries (industry_id),
    CONSTRAINT fk_job_post_admin FOREIGN KEY (approved_by)
        REFERENCES users (user_id),
    CONSTRAINT fk_job_post_province FOREIGN KEY (province_id)
        REFERENCES provinces (province_id),
    CONSTRAINT fk_job_post_administrative_unit FOREIGN KEY (administrative_unit_id)
        REFERENCES administrative_units (unit_id)
);

-- =========================
-- SAVED JOBS
-- =========================
CREATE TABLE saved_jobs
(
    candidate_id INT NOT NULL,
    job_id       INT NOT NULL,
    saved_at     DATETIME2 DEFAULT GETDATE(),

    CONSTRAINT pk_saved_job PRIMARY KEY (candidate_id, job_id),

    CONSTRAINT fk_saved_job_candidate FOREIGN KEY (candidate_id)
        REFERENCES candidate_profiles (candidate_id),
    CONSTRAINT fk_saved_job_job FOREIGN KEY (job_id)
        REFERENCES job_posts (job_id)
);

-- =========================
-- APPLICATIONS
-- =========================
CREATE TABLE applications
(
    application_id INT IDENTITY(1,1) PRIMARY KEY,
    candidate_id   INT         NOT NULL,
    job_id         INT         NOT NULL,
    cv_id          INT         NOT NULL,
    applied_date   DATETIME2   DEFAULT GETDATE(),
    cover_letter   NVARCHAR(MAX),
    status         VARCHAR(30) NOT NULL DEFAULT 'APPLIED'
        CONSTRAINT ck_application_status CHECK (status IN ('APPLIED', 'UNDER_REVIEW', 'SHORTLISTED', 'INTERVIEWED', 'ACCEPTED', 'REJECTED')),
    note           NVARCHAR(500),

    CONSTRAINT uq_application UNIQUE (candidate_id, job_id),

    CONSTRAINT fk_application_candidate FOREIGN KEY (candidate_id)
        REFERENCES candidate_profiles (candidate_id),
    CONSTRAINT fk_application_job FOREIGN KEY (job_id)
        REFERENCES job_posts (job_id),
    CONSTRAINT fk_application_cv FOREIGN KEY (cv_id)
        REFERENCES cvs (cv_id)
);

-- =========================
-- APPLICATION STATUS HISTORIES
-- =========================
CREATE TABLE application_status_histories
(
    history_id     INT IDENTITY(1,1) PRIMARY KEY,
    application_id INT NOT NULL,
    old_status     VARCHAR(30),
    new_status     VARCHAR(30),
    changed_by     INT NOT NULL,
    changed_at     DATETIME2 DEFAULT GETDATE(),

    CONSTRAINT fk_history_application FOREIGN KEY (application_id)
        REFERENCES applications (application_id),
    CONSTRAINT fk_history_user FOREIGN KEY (changed_by)
        REFERENCES users (user_id)
);

-- =========================
-- INTERVIEWS
-- =========================
CREATE TABLE interviews
(
    interview_id   INT IDENTITY(1,1) PRIMARY KEY,
    application_id INT       NOT NULL,
    interview_date DATETIME2 NOT NULL,
    location       NVARCHAR(255),
    meeting_link   VARCHAR(500),
    note           NVARCHAR(500),
    created_at     DATETIME2 DEFAULT GETDATE(),
    status         VARCHAR(20) DEFAULT 'SCHEDULED'
        CONSTRAINT ck_interview_status CHECK (status IN ('SCHEDULED', 'COMPLETED', 'CANCELLED')),

    CONSTRAINT fk_interview_application FOREIGN KEY (application_id)
        REFERENCES applications (application_id)
);

-- =========================
-- VERIFICATION TOKENS
-- =========================
CREATE TABLE verification_tokens
(
    token_id    INT IDENTITY(1,1) PRIMARY KEY,
    token       VARCHAR(255) NOT NULL UNIQUE,
    user_id     INT NOT NULL UNIQUE,
    expiry_date DATETIME2 NOT NULL,

    CONSTRAINT fk_verification_tokens_user FOREIGN KEY (user_id)
        REFERENCES users (user_id) ON DELETE CASCADE
);
