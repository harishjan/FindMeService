PRAGMA foreign_keys=OFF;
BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "UserRole" (
	"userRoleId"	INTEGER NOT NULL UNIQUE,
	"userId"	INTEGER NOT NULL,
	"roleId"	INTEGER NOT NULL,
	PRIMARY KEY("userRoleId" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "SiteReview" (
	"userId"	INTEGER NOT NULL UNIQUE,
	"reviewId"	INTEGER NOT NULL,
	"title"	TEXT NOT NULL,
	"comment"	TEXT NOT NULL,
	"submittedDate"	TEXT NOT NULL,
	"reviewedDate"	TEXT,
	PRIMARY KEY("userId" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "WorkerSkill" (
	"workerSkillId"	INTEGER NOT NULL UNIQUE,
	"workerSkillName"	TEXT NOT NULL,
	PRIMARY KEY("workerSkillId" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "WorkInquiry" (
	"inquiryId"	INTEGER NOT NULL UNIQUE,
	"isCommitted"	INTEGER NOT NULL DEFAULT 0,
	"committedDate"	TEXT,
	"workStartDate"	TEXT NOT NULL,
	"workEndDate"	TEXT NOT NULL,
	"helpFinderUserId"	INTEGER NOT NULL,
	"workerUser"	INTEGER NOT NULL,
	"hiredDate"	REAL,
	"isHired"	INTEGER NOT NULL DEFAULT 0,
	PRIMARY KEY("inquiryId" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "UserWorkSkill" (
	"userWorkSkillId"	INTEGER NOT NULL UNIQUE,
	"workSkillId"	INTEGER NOT NULL,
	"userId"	INTEGER NOT NULL,
	PRIMARY KEY("userWorkSkillId" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "User" (
	"userId"	INTEGER NOT NULL UNIQUE,
	"address"	TEXT,
	"lat"	TEXT,
	"firstName"	TEXT NOT NULL,
	"lastName"	TEXT NOT NULL,
	"emailAddress"	TEXT NOT NULL UNIQUE,
	"userName"	TEXT NOT NULL UNIQUE,
	"password"	TEXT NOT NULL DEFAULT 'test!!!',
	"long"	INTEGER,
	PRIMARY KEY("userId" AUTOINCREMENT)
);

INSERT INTO WorkerSkill
(workerSkillName)
VALUES('Kitchen work');

INSERT INTO WorkerSkill
(workerSkillName)
VALUES('Handy Man');

INSERT INTO WorkerSkill
(workerSkillName)
VALUES('Gardening');

INSERT INTO WorkerSkill
(workerSkillName)
VALUES('plumbing');


INSERT INTO WorkerSkill
(workerSkillName)
VALUES('Cleaning');


DELETE FROM sqlite_sequence;
INSERT INTO sqlite_sequence VALUES('User',0);
COMMIT;




 
        insert into WorkerSkill(workerskillname) values("HandyMan");
        insert into WorkerSkill(workerskillname) values("Painter");
        insert into WorkerSkill(workerskillname) values("Cooking");
        insert into WorkerSkill(workerskillname) values("Housecleaning");
        insert into WorkerSkill(workerskillname) values("Maintenance");
        insert into WorkerSkill(workerskillname) values("Plumber");
        insert into WorkerSkill(workerskillname) values("Electrician");
        insert into WorkerSkill(workerskillname) values("Carpenter");
        insert into WorkerSkill(workerskillname) values("SnowRemoval");
        insert into WorkerSkill(workerskillname) values("Demolition");
        insert into WorkerSkill(workerskillname) values("DebrisRemoval");
        insert into WorkerSkill(workerskillname) values("UnloadingAndLoading");
        insert into WorkerSkill(workerskillname) values("ForkliftOperation");
        insert into WorkerSkill(workerskillname) values("HandToolsPowerTools");
        insert into WorkerSkill(workerskillname) values("FarmAndFieldWork");
        insert into WorkerSkill(workerskillname) values("Catering");
        insert into WorkerSkill(workerskillname) values("GrassCutting");
        insert into WorkerSkill(workerskillname) values("Construction");
        insert into WorkerSkill(workerskillname) values("SecurityGuard");
        insert into WorkerSkill(workerskillname) values("Chauffeur");
        insert into WorkerSkill(workerskillname) values("Butler");
        insert into WorkerSkill(workerskillname) values("Janitor");
        insert into WorkerSkill(workerskillname) values("AthleticTrainer");