package board.aop;

import java.util.Collections;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.MatchAlwaysTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

@Configuration
public class TransactionAspect {
  private static final String AOP_TRANSACTION_METHOD_NAME = "*";
  private static final String AOP_TRANSACTION_EXPRESSION = "execution(* board..service.*Impl.*(..))";

  @Autowired
  private PlatformTransactionManager transactionManager;

  @Bean
  public TransactionInterceptor transactionAdvice() {
    MatchAlwaysTransactionAttributeSource source = new MatchAlwaysTransactionAttributeSource();
    RuleBasedTransactionAttribute transactionAttribute = new RuleBasedTransactionAttribute();

    transactionAttribute.setName(AOP_TRANSACTION_METHOD_NAME);
    // 예외 발생 시 롤백
    transactionAttribute.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
    source.setTransactionAttribute(transactionAttribute);

    return new TransactionInterceptor(transactionManager, source);
  }

  @Bean
  public Advisor transactionAdviceAdvisor() {
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    // 모든 ServiceImpl 클래스의 모든 메서드에 포인트컷을 설정함
    pointcut.setExpression(AOP_TRANSACTION_EXPRESSION);
    return new DefaultPointcutAdvisor(pointcut, transactionAdvice());
  }
}