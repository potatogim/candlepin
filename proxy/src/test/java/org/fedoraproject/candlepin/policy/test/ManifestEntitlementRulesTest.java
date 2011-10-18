/**
 * Copyright (c) 2009 Red Hat, Inc.
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
package org.fedoraproject.candlepin.policy.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.fedoraproject.candlepin.config.Config;
import org.fedoraproject.candlepin.model.Consumer;
import org.fedoraproject.candlepin.model.ConsumerType;
import org.fedoraproject.candlepin.model.Entitlement;
import org.fedoraproject.candlepin.model.Owner;
import org.fedoraproject.candlepin.model.Pool;
import org.fedoraproject.candlepin.model.PoolAttribute;
import org.fedoraproject.candlepin.model.Product;
import org.fedoraproject.candlepin.model.ProductAttribute;
import org.fedoraproject.candlepin.model.Rules;
import org.fedoraproject.candlepin.model.RulesCurator;
import org.fedoraproject.candlepin.policy.js.JsRules;
import org.fedoraproject.candlepin.policy.js.JsRulesProvider;
import org.fedoraproject.candlepin.policy.js.ReadOnlyPool;
import org.fedoraproject.candlepin.policy.js.compliance.ComplianceStatus;
import org.fedoraproject.candlepin.policy.js.entitlement.ManifestEntitlementRules;
import org.fedoraproject.candlepin.policy.js.entitlement.PreEntHelper;
import org.fedoraproject.candlepin.policy.js.pool.PoolHelper;
import org.fedoraproject.candlepin.service.ProductServiceAdapter;
import org.fedoraproject.candlepin.test.DatabaseTestFixture;
import org.fedoraproject.candlepin.test.DateSourceForTesting;
import org.fedoraproject.candlepin.test.TestDateUtil;
import org.fedoraproject.candlepin.test.TestUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * CandlepinConsumerTypeEnforcerTest
 */
public class ManifestEntitlementRulesTest extends DatabaseTestFixture {

    @Mock private ProductServiceAdapter productAdapter;
    @Mock private RulesCurator rulesCurator;
    @Mock private Config config;
    @Mock private ComplianceStatus compliance;

    private ManifestEntitlementRules enforcer;
    private Owner owner;
    private Consumer consumer;
    private JsRules jsRules;
    private static final String LONGEST_EXPIRY_PRODUCT = "LONGEST001";
    private static final String HIGHEST_QUANTITY_PRODUCT = "QUANTITY001";
    private static final String BAD_RULE_PRODUCT = "BADRULE001";
    private static final String PRODUCT_CPULIMITED = "CPULIMITED001";

    @Before
    public void createEnforcer() throws Exception {
        MockitoAnnotations.initMocks(this);

        owner = createOwner();
        ownerCurator.create(owner);

        consumer = TestUtil.createConsumer(owner);
        consumerTypeCurator.create(consumer.getType());
        consumerCurator.create(consumer);

        BufferedReader reader
            = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream("/rules/test-rules.js")));
        StringBuilder builder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            builder.append(line + "\n");
        }
        reader.close();

        Rules rules = mock(Rules.class);
        when(rules.getRules()).thenReturn(builder.toString());
        when(rulesCurator.getRules()).thenReturn(rules);
        when(rulesCurator.getUpdated()).thenReturn(TestDateUtil.date(2010, 1, 1));

        jsRules = new JsRulesProvider(rulesCurator).get();

        enforcer = new ManifestEntitlementRules(new DateSourceForTesting(2010, 1, 1),
            jsRules, productAdapter, i18n, config, consumerCurator);
    }

    @Test
    public void postEntitlement() {
        Consumer c = mock(Consumer.class);
        PoolHelper ph = mock(PoolHelper.class);
        Entitlement e = mock(Entitlement.class);
        ConsumerType type = mock(ConsumerType.class);
        Pool pool = mock(Pool.class);
        Product product = mock(Product.class);
        
        when(e.getPool()).thenReturn(pool);
        when(e.getConsumer()).thenReturn(c);
        when(c.getType()).thenReturn(type);
        when(type.isManifest()).thenReturn(true);
        when(pool.getProductId()).thenReturn("testProd");
        when(productAdapter.getProductById(eq("testProd"))).thenReturn(product);
        when(product.getAttributes()).thenReturn(new HashSet<ProductAttribute>());
        when(pool.getAttributes()).thenReturn(new HashSet<PoolAttribute>());
               
        assertEquals(ph, enforcer.postEntitlement(c, ph, e));
    }

    @Test
    public void preEntitlement() {
        Consumer c = mock(Consumer.class);
        Pool p = mock(Pool.class);
        ReadOnlyPool roPool = mock(ReadOnlyPool.class);
        PreEntHelper peh = enforcer.preEntitlement(c, p, 10);
        assertNotNull(peh);
        peh.checkQuantity(roPool);
        verify(roPool).entitlementsAvailable(eq(1));
    }

    @Test(expected = NullPointerException.class)
    public void bestPoolsNull() {
        enforcer.selectBestPools(null, null, null, compliance);
    }

    @Test
    public void bestPoolEmpty() {
        assertEquals(null,
            enforcer.selectBestPools(null, null, new ArrayList<Pool>(), compliance));
    }

    @Test
    public void bestPool() {
        Map<Pool, Integer> pools = new HashMap<Pool, Integer>();
        List<Pool> allPools = new ArrayList<Pool>();
        allPools.add(mock(Pool.class));
        pools.put(allPools.get(0), 1);
        assertEquals(pools, enforcer.selectBestPools(null, null, allPools, compliance));
    }

}
