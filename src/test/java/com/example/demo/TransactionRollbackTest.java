package com.example.demo;

import com.example.demo.account.AccountMapper;
import com.example.demo.common.exception.BuisinessException; // 실제 패키지 경로에 맞게 확인해주세요
import com.example.demo.transaction.HistoryMapper;
import com.example.demo.transaction.dto.TransferRequest;
import com.example.demo.transaction.service.TransactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

// ⚠️ 일부러 @Transactional을 안 붙입니다.
// transfer()가 자기만의 독립된 트랜잭션으로 실행되고 "실제로 롤백까지 끝난 뒤" 상태를 확인해야
// 진짜 롤백 검증이 되기 때문입니다. (자세한 이유는 대화 설명 참고)
@SpringBootTest
class TransactionRollbackTest {

    @Autowired
    TransactionService transactionService;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    HistoryMapper historyMapper;

    // findByAccountId는 페이지네이션용(LIMIT/OFFSET)이라, 여기선 size를 충분히 크게 줘서
    // "이 계좌의 전체 히스토리 개수"를 세는 용도로 재사용합니다.
    private int countHistories(Long accountId) {
        return historyMapper.findByAccountId(accountId, Integer.MAX_VALUE, 0).size();
    }

    @Test
    @DisplayName("✅ 이체 중 예외 발생 시 출금도 롤백된다")
    void 이체_실패시_전체_롤백() {
        BigDecimal before1 = accountMapper.findById(1L).getBalance();
        BigDecimal before2 = accountMapper.findById(2L).getBalance();
        int historyBefore = countHistories(1L);

        // 존재하지 않는 계좌번호 → 이체 도중 예외 발생
        assertThatThrownBy(() -> transactionService.transfer(
                new TransferRequest(1L, "9999999999", new BigDecimal("1000"), "1234"), 1L))
                .isInstanceOf(BuisinessException.class);

        assertThat(accountMapper.findById(1L).getBalance()).isEqualByComparingTo(before1);
        assertThat(accountMapper.findById(2L).getBalance()).isEqualByComparingTo(before2);
        assertThat(countHistories(1L)).isEqualTo(historyBefore);   // 원장도 안 남아야 함 ⭐
    }
}