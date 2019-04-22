package by.tut.mdcatalog.service.aspect;

import by.tut.mdcatalog.data.resources.AuditItemRepository;
import by.tut.mdcatalog.data.resources.exception.PreparedStatementException;
import by.tut.mdcatalog.data.resources.model.AuditItem;
import by.tut.mdcatalog.data.resources.model.Item;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import java.sql.Connection;
import java.sql.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
@Component
public class Advice {
    private static final Logger logger = LoggerFactory.getLogger(Advice.class);
    private final AuditItemRepository auditItemRepository;

    public Advice(AuditItemRepository auditItemRepository) {
        this.auditItemRepository = auditItemRepository;
    }

    @Pointcut("execution(* by.tut.mdcatalog..*.*(..))")
    public void addingItemPointCut() {
    }

    @AfterReturning(value = "addingItemPointCut()", returning = "returnedItem")
    public void logAddingItem(JoinPoint joinPoint, Item returnedItem) throws PreparedStatementException {
        AuditItem auditItem = new AuditItem();
        auditItem.setDate(new Date(System.currentTimeMillis()));
        auditItem.setAction("Completed");
        auditItem.setItemID(returnedItem.getId());
        Connection connection = (Connection) joinPoint.getArgs()[0];
        logger.info("Item has been saved: {}", auditItemRepository.add(connection, auditItem));
    }

    @Pointcut("execution(* by.tut.mdcatalog..*.*(..))")
    public void updatingItemStatusPointCut() {
    }

    @AfterReturning(value = "updatingItemStatusPointCut()", returning = "affectedRows")
    public void logUpdatingItemStatus(JoinPoint joinPoint, int affectedRows) throws PreparedStatementException {
        if (affectedRows == 1) {
            AuditItem auditItem = new AuditItem();
            auditItem.setDate(new Date(System.currentTimeMillis()));
            auditItem.setAction("Item updated");
            Connection connection = (Connection) joinPoint.getArgs()[0];
            auditItem.setItemID(Long.parseLong(joinPoint.getArgs()[1].toString()));
            logger.info("Item has been saved: {}", auditItemRepository.add(connection, auditItem));
        }
    }
}
