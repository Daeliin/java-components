package com.blebail.components.cms.credentials.account;

import com.blebail.components.cms.sql.BAccount;
import com.blebail.components.core.resource.Conversion;

public final class AccountConversion implements Conversion<Account, BAccount> {

    public Account from(BAccount bAccount) {
        return new Account(
            bAccount.getId(),
            bAccount.getCreationDate(),
            bAccount.getUsername(),
            bAccount.getEmail(),
            bAccount.getEnabled(),
            bAccount.getPassword(),
            bAccount.getToken());
    }

    @Override
    public BAccount to(Account account) {
        return new BAccount(
            account.creationDate(),
            account.email,
            account.enabled,
            account.id(),
            account.password,
            account.token,
            account.username);
    }
}
