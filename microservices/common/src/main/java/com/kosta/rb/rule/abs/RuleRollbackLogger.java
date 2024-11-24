package com.kosta.rb.rule.abs;

public interface RuleRollbackLogger {
    void rollBackPrevLog();
    void rollBackCloseLog();
}
