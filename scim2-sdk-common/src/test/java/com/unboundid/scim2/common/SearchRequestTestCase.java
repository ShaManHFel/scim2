/*
 * Copyright 2015 UnboundID Corp.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License (GPLv2 only)
 * or the terms of the GNU Lesser General Public License (LGPLv2.1 only)
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 */

package com.unboundid.scim2.common;

import com.unboundid.scim2.common.exceptions.ScimException;
import com.unboundid.scim2.common.messages.SearchRequest;
import com.unboundid.scim2.common.messages.SortOrder;
import com.unboundid.scim2.common.utils.SchemaUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.testng.Assert.assertEquals;

/**
 * Test for search requests.
 */
public class SearchRequestTestCase
{
  /**
   * Test search request.
   *
   * @throws java.io.IOException If an error occurs.
   * @throws ScimException If an error occurs.
   */
  @Test
  public void testSearchRequest() throws IOException, ScimException
  {
    SearchRequest searchRequest = SchemaUtils.createSCIMCompatibleMapper().
        readValue("{\n" +
            "     \"schemas\": [" +
            "\"urn:ietf:params:scim:api:messages:2.0:SearchRequest\"],\n" +
            "     \"attributes\": [\"displayName\", \"userName\"],\n" +
            "     \"filter\":\n" +
            "       \"displayName sw \\\"smith\\\"\",\n" +
            "     \"startIndex\": 1,\n" +
            "     \"count\": 10\n" +
            "}", SearchRequest.class);

    assertEquals(searchRequest.getAttributes(),
        Arrays.asList("displayName", "userName"));
    assertEquals(searchRequest.getExcludedAttributes(), null);
    assertEquals(searchRequest.getFilter(), "displayName sw \"smith\"");
    assertEquals(searchRequest.getSortBy(), null);
    assertEquals(searchRequest.getSortOrder(), null);
    assertEquals(searchRequest.getStartIndex(), new Long(1));
    assertEquals(searchRequest.getCount(), new Long(10));

    searchRequest = SchemaUtils.createSCIMCompatibleMapper().
        readValue("{\n" +
            "     \"schemas\": [" +
            "\"urn:ietf:params:scim:api:messages:2.0:SearchRequest\"],\n" +
            "     \"excludedAttributes\": [\"displayName\", \"userName\"],\n" +
            "     \"sortBy\": \"name.lastName\",\n" +
            "     \"sortOrder\": \"descending\"\n" +
            "}", SearchRequest.class);

    assertEquals(searchRequest.getAttributes(), null);
    assertEquals(searchRequest.getExcludedAttributes(),
        Arrays.asList("displayName", "userName"));
    assertEquals(searchRequest.getFilter(), null);
    assertEquals(searchRequest.getSortBy(), "name.lastName");
    assertEquals(searchRequest.getSortOrder(), SortOrder.DESCENDING);
    assertEquals(searchRequest.getStartIndex(), null);
    assertEquals(searchRequest.getCount(), null);

    searchRequest = new SearchRequest();
    searchRequest.setAttributes(Arrays.asList("displayName", "userName")).
        setExcludedAttributes(Arrays.asList("addresses")).
        setFilter("userName eq \"test\"").
        setSortBy("name.lastName").
        setSortOrder(SortOrder.ASCENDING).
        setStartIndex(5L).
        setCount(100L);

    String serialized = SchemaUtils.createSCIMCompatibleMapper().
        writeValueAsString(searchRequest);
    assertEquals(SchemaUtils.createSCIMCompatibleMapper().readValue(
        serialized, SearchRequest.class), searchRequest);
  }
}
