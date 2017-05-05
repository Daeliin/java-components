package com.daeliin.components.security.credentials.permission;

import com.daeliin.components.security.fixtures.PermissionFixtures;
import com.daeliin.components.security.library.PermissionLibrary;
import com.daeliin.components.security.sql.BPermission;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public final class PermissionConversionTest {

    private PermissionConversion permissionConversion = new PermissionConversion();

    @Test
    public void shouldMapToNull_whenNull() {
        Permission nullPermission = null;

        assertThat(permissionConversion.map(nullPermission)).isNull();
    }

    @Test
    public void shouldMapPermission() {
        BPermission mappedPermission = permissionConversion.map(PermissionLibrary.admin());

        assertThat(mappedPermission).isEqualToComparingFieldByField(PermissionFixtures.admin());
    }

    @Test
    public void shouldInstantiateNull_fromNull() {
        BPermission nullPermissionRow = null;

        assertThat(permissionConversion.instantiate(nullPermissionRow)).isNull();
    }

    @Test
    public void shouldInstantiateAnPermission() {
        Permission rebuiltPermission = permissionConversion.instantiate(PermissionFixtures.admin());

        assertThat(rebuiltPermission).isEqualTo(PermissionLibrary.admin());
    }
}