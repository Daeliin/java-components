package com.daeliin.components.core.event;

import com.daeliin.components.domain.resource.PersistentResource;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Java6Assertions.assertThat;

public final class EventLogTest {

    @Test
    public void shouldExtendPersistentResource() {
        Assertions.assertThat(EventLog.class.getSuperclass().getClass()).isEqualTo(PersistentResource.class.getClass());
    }

    @Test(expected = Exception.class)
    public void shouldThrowException_whenDescriptionKeyIsNull() {
        new EventLog(UUID.randomUUID().toString(), LocalDateTime.now(), null);
    }

    @Test
    public void shouldAssignADescriptionKey() {
        EventLog eventLog = new EventLog(UUID.randomUUID().toString(), LocalDateTime.now(), "descriptionKey");

        assertThat(eventLog.descriptionKey).isEqualTo("descriptionKey");
    }

    @Test
    public void shouldPrintsItsIdUuidAndCreationDate() {
        EventLog eventLog = new EventLog(UUID.randomUUID().toString(), LocalDateTime.now(), "descriptionKey");

        assertThat(eventLog.toString()).contains(String.valueOf(eventLog.descriptionKey));
    }

    @Test
    public void shouldBeComparedOnCreationDate() {
        LocalDateTime creationDate1 = LocalDateTime.now();
        LocalDateTime creationDate2 = LocalDateTime.now().plus(10, ChronoUnit.SECONDS);

        EventLog eventLog1 = new EventLog(UUID.randomUUID().toString(), creationDate1, "descriptionKey");
        EventLog eventLog2 = new EventLog(UUID.randomUUID().toString(), creationDate2, "descriptionKey");

        assertThat(eventLog1.compareTo(eventLog2)).isNegative();
        assertThat(eventLog2.compareTo(eventLog1)).isPositive();
    }
}