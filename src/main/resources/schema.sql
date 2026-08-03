DROP TABLE IF EXISTS history;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS member;


create table member(
    id BIGINT auto_increment primary key,
    user_id varchar(50) not null unique comment '로그인 ID',
    password varchar(100) not null comment 'BCrypt 해시',
    user_name varchar(50) not null comment '실명',
    email varchar(100),
    created_at datetime not null default CURRENT_TIMESTAMP,
    updated_at datetime
);

create table account(
    id bigint auto_increment primary key,
    account_number varchar(20) not null comment '계좌번호',
    password varchar(100) not null comment '계좌 비번 BCrypt 해시',
    balance decimal(18,2) not null default 0,
    member_id bigint not null,
    created_at datetime not null default CURRENT_TIMESTAMP,
    updated_at datetime,
    constraint fk_account_member foreign key(member_id) references member(id)

);

CREATE INDEX idx_account_member ON account(member_id);

create table history(
    id bigint auto_increment primary key comment '거래내역 id',
    tx_type varchar(20) not null comment 'deposit/withdraw',
    amount bigint not null comment '거래금액',
    w_account_id int comment '출금 계좌 ID',
    d_account_id int comment '입금 계좌 ID',
    w_balance bigint comment '출금 요청 후 계좌 잔액',
    d_balance bigint comment '입금 요청 후 계좌 잔액',
    created_at datetime not null default CURRENT_TIMESTAMP,

    constraint fk_history_w foreign key (w_account_id) references account(id),
    constraint fk_history_d foreign key (d_account_id) references account(id),
    constraint ck_history_amount check (amount > 0),
    constraint ck_history_exists check (w_account_id IS NOT NULL OR d_account_id IS NOT NULL),
    --자기 계좌로 이체 방지
    CONSTRAINT ck_history_notself CHECK (w_account_id IS NULL OR d_account_id IS NULL
                                          OR w_account_id <> d_account_id)
);

CREATE INDEX idx_history_w ON history(w_account_id, created_at);
CREATE INDEX idx_history_d ON history(d_account_id, created_at);


