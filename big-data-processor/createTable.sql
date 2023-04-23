create table manager
(
    oid            int auto_increment
        primary key,
    task_no        int         not null,
    userid         mediumtext  null,
    name           varchar(32) null,
    process_status varchar(10) null
);

create index manager_task_no_index
    on manager (task_no);


create table odometer
(
    oid  int auto_increment
        primary key,
    id   int         not null,
    name varchar(32) null
)
    comment '生成 id';

create table task
(
    oid           int auto_increment
        primary key,
    task_no       int           not null,
    total_count   int default 0 not null,
    pending_count int default 0 not null,
    success_count int default 0 not null,
    fail_count    int default 0 not null,
    task_status   varchar(10)   not null
);

create index task_task_no_index
    on task (task_no);