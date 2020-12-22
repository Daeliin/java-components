package com.blebail.components.cms.membership.notifications;

import com.blebail.components.cms.credentials.account.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!mail")
public class LoggingMembershipNotifications implements MembershipNotifications {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingMembershipNotifications.class);

    @Override
    public void onSignUp(Account account) {
        LOGGER.info("The account " + account + " signed up");
    }

    @Override
    public void onActivate(Account account) {
        LOGGER.info("The account " + account + " activated");
    }

    @Override
    public void onNewPassword(Account account) {
        LOGGER.info("The account " + account + " requested a new password");
    }

    @Override
    public void onResetPassword(Account account) {
        LOGGER.info("The account " + account + " reseted its password");
    }
}
