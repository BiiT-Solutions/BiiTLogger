package com.biit.logger;

/*-
 * #%L
 * BiiT Logger
 * %%
 * Copyright (C) 2015 - 2025 BiiT Sourcing Solutions S.L.
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import junit.framework.Assert;
import org.testng.annotations.Test;

@Test(groups = {"inheritance"})
public class InheritanceTest {

    @Test
    public void checkLoggerName() {
        BiitLogger childLogger = new ChildLogger();
        Assert.assertEquals(ChildLogger.class.getName(), childLogger.getLogger().getName());
    }
}
