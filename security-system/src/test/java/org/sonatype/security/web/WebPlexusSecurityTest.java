/**
 * Copyright (c) 2008 Sonatype, Inc. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package org.sonatype.security.web;

import java.util.List;

import junit.framework.Assert;

import org.jsecurity.mgt.RealmSecurityManager;
import org.jsecurity.realm.CachingRealm;
import org.jsecurity.realm.Realm;
import org.jsecurity.realm.SimpleAccountRealm;
import org.sonatype.security.AbstractSecurityTest;
import org.sonatype.security.SecuritySystem;

public class WebPlexusSecurityTest
    extends AbstractSecurityTest
{

    public void testCacheManagerInit()
        throws Exception
    {
        // Start up security
        SecuritySystem securitySystem = this.lookup( SecuritySystem.class );
        
        RealmSecurityManager realmSecurityManager = this.lookup( RealmSecurityManager.class, "web" );
        
        List<String> realms = securitySystem.getRealms();
        realms.clear();
        realms.add( SimpleAccountRealm.class.getName() );
        securitySystem.setRealms( realms );

        // now if we grab one of the realms from the Realm locator, it should have its cache set
        CachingRealm cRealm1 = (CachingRealm) realmSecurityManager.getRealms().iterator().next();
        Assert.assertNotNull( "Realm has null cacheManager", cRealm1.getCacheManager() );

        // // so far so good, the cacheManager should be set on all the child realms, but what if we add one after the
        // init method?
        realms.add( SimpleAccountRealm.class.getName() );
        securitySystem.setRealms( realms );
        
        // this list should have exactly 2 elements
        Assert.assertEquals( 2, realmSecurityManager.getRealms().size() );
        
        for ( Realm realm : realmSecurityManager.getRealms() )
        {
            Assert.assertNotNull( "Realm has null cacheManager", ((CachingRealm) realm).getCacheManager() );
        }
    }

}
