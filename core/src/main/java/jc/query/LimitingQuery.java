/**
 * Copyright 2013 the project contributors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package jc.query;

import java.util.HashMap;
import java.util.Map;

public class LimitingQuery<T> implements Queryable<T>, Pageable, Sortable, Filterable {
    
    public static final String ATTRIBUTE_OFFSET = "offset";
    public static final String ATTRIBUTE_LIMIT = "limit";
    public static final String ATTRIBUTE_SORTBY_ASC = "sortByAsc";
    public static final String ATTRIBUTE_SORTBY_DESC = "sortByDesc";
    
    protected static final ThreadLocal<LimitingQuery<?>> QUERIES = new ThreadLocal<LimitingQuery<?>>() {
        
        @Override
        protected LimitingQuery<?> initialValue() {
            return new LimitingQuery<Object>(null, 0, 0);
        }
        
    };
    
    @SuppressWarnings("unchecked")
    public static <K> LimitingQuery<K> getCurrentQuery() {
        return (LimitingQuery<K>) QUERIES.get();
    }
    
    @SuppressWarnings("unchecked")
    public static LimitingQuery<String> getIdQuery(String id) {
        LimitingQuery<String> query = (LimitingQuery<String>) QUERIES.get();
        
        query.setKey(id);
        query.setLimit(-1);
        query.setOffset(0);
        
        return query;
    }
    
    protected T key;
    protected long limit = -1;
    protected long offset = 0;
    protected Map<String, Boolean> sortFields = new HashMap<String, Boolean>();
    protected Map<String, Boolean> filteredFields = new HashMap<String, Boolean>();
    
    public LimitingQuery(T key, long limit, long offset) {
        this.key = key;
        this.limit = limit;
        this.offset = offset;
    }
    
    public void setKey(T key) {
        this.key = key;
    }
    
    public void setLimit(long limit) {
        this.limit = limit;
    }
    
    public void setOffset(long offset) {
        this.offset = offset;
    }
    
    @Override
    public long getOffset() {
        return offset;
    }
    
    @Override
    public long getLimit() {
        return limit;
    }
    
    @Override
    public T getKey() {
        return key;
    }
    
    @Override
    public Map<String, Boolean> getSortFields() {
        return sortFields;
    }
    
    @Override
    public Map<String, Boolean> getFilteredFields() {
        return filteredFields;
    }
    
    public void setFilteredFields(Map<String, Boolean> filteredFields) {
        this.filteredFields = filteredFields;
    }
    
    public void setSortFields(Map<String, Boolean> sortFields) {
        this.sortFields = sortFields;
    }
    
}
