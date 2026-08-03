-- 비밀번호는 개발용 평문. 실제로는 반드시 BCrypt 해시 저장
INSERT INTO member (user_id, password, user_name, email) VALUES
    ('gildong', '1234', '홍길동', 'gildong@test.com'),
    ('dooly',   '1234', '둘리',   'dooly@test.com'),
    ('mai',     '1234', '마이',   'mai@test.com');

INSERT INTO account (account_number, password, balance, member_id) VALUES
    ('1111', '1234', 1200, 1),
    ('2222', '1234', 1500, 2),
    ('3333', '1234',    0, 3);

-- history 합계가 위 balance와 정확히 일치합니다
INSERT INTO history (tx_type, amount, w_account_id, d_account_id, w_balance, d_balance) VALUES
    ('DEPOSIT',  2000, NULL, 1,    NULL, 2000),
    ('DEPOSIT',  1000, NULL, 2,    NULL, 1000),
    ('WITHDRAW',  300, 1,    NULL, 1700, NULL);