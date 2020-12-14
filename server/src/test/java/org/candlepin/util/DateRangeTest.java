/**
 * Copyright (c) 2009 - 2012 Red Hat, Inc.
 *
 * This software is licensed to you under the GNU General Public License,
 * version 2 (GPLv2). There is NO WARRANTY for this software, express or
 * implied, including the implied warranties of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
 * along with this software; if not, see
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
 *
 * Red Hat trademarks are not licensed under GPLv2. No permission is
 * granted to use or replicate Red Hat trademarks that are incorporated
 * in this software or its documentation.
 */
package org.candlepin.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.candlepin.dto.api.v1.DateRange;
import org.candlepin.test.TestDateUtil;

import org.junit.Test;

import java.util.Date;



/**
 * DateRangeTest
 */
public class DateRangeTest {

    @Test
    public void getters() {
        Date start = TestDateUtil.date(2012, 5, 22);
        Date end = TestDateUtil.date(2012, 7, 4);
        DateRange range = new DateRange();
        range.startDate(Util.toDateTime(start)).endDate(Util.toDateTime(end));
        assertEquals(start, Util.toDate(range.getStartDate()));
        assertEquals(end, Util.toDate(range.getEndDate()));
    }

    @Test
    public void contains() {
        Date start = TestDateUtil.date(2001, 7, 5);
        Date end = TestDateUtil.date(2010, 7, 4);
        DateRange range = new DateRange();
        range.startDate(Util.toDateTime(start)).endDate(Util.toDateTime(end));
        assertTrue(contains(TestDateUtil.date(2005, 6, 9), start, end));
        assertFalse(contains(TestDateUtil.date(1971, 7, 19), start, end));
        assertFalse(contains(TestDateUtil.date(2012, 4, 19), start, end));
        assertTrue(contains(TestDateUtil.date(2001, 7, 5), start, end));
        assertTrue(contains(TestDateUtil.date(2010, 7, 4), start, end));
    }

    private boolean contains(Date date, Date startDate, Date endDate) {
        return (startDate != null && startDate.compareTo(date) <= 0) &&
            (endDate != null && endDate.compareTo(date) >= 0);
    }
}
