/*
 * Copyright © 2014 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package co.cask.tigon.data.runtime;

import co.cask.tephra.TxConstants;
import co.cask.tephra.distributed.PooledClientProvider;
import co.cask.tephra.distributed.ThreadLocalClientProvider;
import co.cask.tephra.distributed.ThriftClientProvider;
import co.cask.tephra.metrics.TxMetricsCollector;
import co.cask.tephra.runtime.TransactionModules;
import co.cask.tigon.conf.CConfiguration;
import co.cask.tigon.data.queue.QueueClientFactory;
import co.cask.tigon.data.transaction.metrics.TransactionManagerMetricsCollector;
import co.cask.tigon.data.transaction.queue.QueueAdmin;
import co.cask.tigon.data.transaction.queue.hbase.HBaseQueueAdmin;
import co.cask.tigon.data.transaction.queue.hbase.HBaseQueueClientFactory;
import co.cask.tigon.data.util.hbase.HBaseTableUtil;
import co.cask.tigon.data.util.hbase.HBaseTableUtilFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import org.apache.hadoop.conf.Configuration;
import org.apache.twill.discovery.DiscoveryServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Defines guice bindings for distributed modules.
 */
public class DataFabricDistributedModule extends AbstractModule {

  private static final Logger LOG = LoggerFactory.getLogger(DataFabricDistributedModule.class);

  public DataFabricDistributedModule() {

  }

  @Override
  public void configure() {
    bind(ThriftClientProvider.class).toProvider(ThriftClientProviderSupplier.class);
    bind(QueueClientFactory.class).to(HBaseQueueClientFactory.class).in(Singleton.class);
    bind(QueueAdmin.class).to(HBaseQueueAdmin.class).in(Singleton.class);
    bind(HBaseTableUtil.class).toProvider(HBaseTableUtilFactory.class);

    // bind transactions
    bind(TxMetricsCollector.class).to(TransactionManagerMetricsCollector.class).in(Scopes.SINGLETON);
    install(new TransactionModules().getDistributedModules());
  }

  /**
   * Provides implementation of {@link ThriftClientProvider} based on configuration.
   */
  @Singleton
  private static final class ThriftClientProviderSupplier implements Provider<ThriftClientProvider> {

    private final CConfiguration cConf;
    private final Configuration hConf;
    private DiscoveryServiceClient discoveryServiceClient;

    @Inject
    ThriftClientProviderSupplier(CConfiguration cConf, Configuration hConf) {
      this.cConf = cConf;
      this.hConf = hConf;
    }

    @Inject(optional = true)
    void setDiscoveryServiceClient(DiscoveryServiceClient discoveryServiceClient) {
      this.discoveryServiceClient = discoveryServiceClient;
    }

    @Override
    public ThriftClientProvider get() {
      // configure the client provider
      String provider = cConf.get(TxConstants.Service.CFG_DATA_TX_CLIENT_PROVIDER,
                                  TxConstants.Service.DEFAULT_DATA_TX_CLIENT_PROVIDER);
      ThriftClientProvider clientProvider;
      if ("pool".equals(provider)) {
        clientProvider = new PooledClientProvider(hConf, discoveryServiceClient);
      } else if ("thread-local".equals(provider)) {
        clientProvider = new ThreadLocalClientProvider(hConf, discoveryServiceClient);
      } else {
        String message = "Unknown Transaction Service Client Provider '" + provider + "'.";
        LOG.error(message);
        throw new IllegalArgumentException(message);
      }
      return clientProvider;
    }
  }
}
