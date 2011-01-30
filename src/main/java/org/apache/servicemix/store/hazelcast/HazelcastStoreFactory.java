/*
 * Copyright 2011 iocanel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * under the License.
 */

package org.apache.servicemix.store.hazelcast;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IdGenerator;
import java.io.IOException;
import java.util.Map;
import org.apache.servicemix.store.Store;


/**
 *
 * @author iocanel
 */
public class HazelcastStoreFactory {
        
    private Map<String, HazelcastStore> stores = Hazelcast.getMap(STORE_PREFIX);
    private long timeout = -1;
    
    public static final String STORE_PREFIX="org.apache.servicemix.stores";
    
    public synchronized Store open(String name) throws IOException {
        HazelcastStore store = stores.get(name);
        String storeName = STORE_PREFIX+"."+name;
        if (store == null) {
            if (timeout <= 0) {
                store = new HazelcastStore(Hazelcast.getIdGenerator(storeName),storeName);
            } else {
                store = new HazelcastStore(Hazelcast.getIdGenerator(storeName), storeName, timeout);
            }
            stores.put(name, store);
        }
        return store;
    }
    
    /* (non-Javadoc)
     * @see org.apache.servicemix.store.ExchangeStoreFactory#release(org.apache.servicemix.store.ExchangeStore)
     */
    public synchronized void close(Store store) throws IOException {
        stores.remove(store);
    }
    
    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
    

}
