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

package com.unboundid.scim2.extension.messages.consent;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.unboundid.scim2.common.utils.JsonUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.URL;

@Test
public class ConsentTest
{
  /**
   * Tests serialization of Consent objects.
   *
   * @throws Exception if an error occurs.
   */
  @Test
  public void testSerialization() throws Exception
  {
    Application application = new Application.Builder().
        setName("appName").setDescription("appDesc").
        setEmailAddress("email@address.com").
        setIconUrl(new URL("http://localhost:12345/app/icon")).
        setIconUrl(new URL("http://localhost:12345/app")).build();

    Scope scope1 = new Scope.Builder().setConsent(Scope.CONSENT_GRANTED).
        setName("name1").setDescription("description1").build();
    Scope scope2 = new Scope.Builder().setConsent(Scope.CONSENT_DENIED).
        setName("name2").setDescription("description2").build();
    Scope scope3 = new Scope.Builder().setConsent(Scope.CONSENT_REVOKED).
        setName("name3").setDescription("description3").build();

    String meta_lastModified = "2015-07-06T04:03:02Z";
    String id = "ConsentId";


    ObjectNode objectNode = JsonUtils.getJsonNodeFactory().objectNode();
    objectNode.putPOJO("application", application);
    objectNode.putArray("scope").addPOJO(scope1).addPOJO(scope2).addPOJO(
        scope3);
    objectNode.putObject("meta").put("lastModified", meta_lastModified);
    objectNode.put("id", id);

    Consent consent1 = JsonUtils.getObjectReader().forType(Consent.class).
        readValue(JsonUtils.getObjectWriter().writeValueAsString(objectNode));
    Assert.assertEquals(application, consent1.getApplication());
    Assert.assertEquals(id, consent1.getId());

    for(Scope scope : consent1.getScopes())
    {
      if(scope.getName().equals(scope1.getName()))
      {
        Assert.assertEquals(scope1, scope);
      }
      else if(scope.getName().equals(scope2.getName()))
      {
        Assert.assertEquals(scope2, scope);
      }
      else if(scope.getName().equals(scope3.getName()))
      {
        Assert.assertEquals(scope3, scope);
      }
      else
      {
        Assert.fail("Unknown scope found - name = " + scope.getName());
      }
    }

    Consent consent2 =
        JsonUtils.getObjectReader().forType(Consent.class).readValue(
            JsonUtils.getObjectWriter().writeValueAsString(consent1));
    Assert.assertEquals(consent1, consent2);
  }
}