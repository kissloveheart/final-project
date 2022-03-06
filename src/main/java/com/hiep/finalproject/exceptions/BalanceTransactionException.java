package com.hiep.finalproject.exceptions;

import java.io.IOException;

public class BalanceTransactionException extends IOException {
    public BalanceTransactionException(String message) {
        super(message);
    }
}
