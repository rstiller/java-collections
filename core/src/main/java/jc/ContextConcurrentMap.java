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
package jc;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import jc.command.ClearCommand;
import jc.command.ContainsCommand;
import jc.command.IsEmptyCommand;
import jc.command.MapEntrySetCommand;
import jc.command.MapGetCommand;
import jc.command.MapKeySetCommand;
import jc.command.MapPutCommand;
import jc.command.MapRemoveCommand;
import jc.command.MapReplaceCommand;
import jc.command.MapValuesCommand;
import jc.command.SizeCommand;


public abstract class ContextConcurrentMap<C extends MapContext<K, V>, K, V> implements ConcurrentMap<K, V> {
    
    protected SizeCommand<C> sizeCommand;
    protected IsEmptyCommand<C> isEmptyCommand;
    protected ContainsCommand<C, K> containsKeyCommand;
    protected ContainsCommand<C, V> containsValueCommand;
    protected ClearCommand<C> clearCommand;
    protected MapKeySetCommand<C, K> keySetCommand;
    protected MapValuesCommand<C, V> valuesCommand;
    protected MapEntrySetCommand<C, K, V> entrySetCommand;
    protected MapGetCommand<C, K, V> getCommand;
    protected MapPutCommand<C, K, V> putCommand;
    protected MapRemoveCommand<C, K, V> removeCommand;
    protected MapReplaceCommand<C, K, V> replaceCommand;
    protected ThreadLocal<C> contexts;
    
    public ContextConcurrentMap() {
        contexts = getContexts();
    }
    
    public C getContext() {
        return contexts.get();
    }
    
    protected abstract ThreadLocal<C> getContexts();
    
    @Override
    public int size() {
        return sizeCommand.size(contexts.get());
    }
    
    @Override
    public boolean isEmpty() {
        return isEmptyCommand.isEmpty(contexts.get());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean containsKey(Object key) {
        return containsKeyCommand.contains(contexts.get(), (K) key);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean containsValue(Object value) {
        return containsValueCommand.contains(contexts.get(), (V) value);
    }
    
    @Override
    public void clear() {
        clearCommand.clear(contexts.get());
    }
    
    @Override
    public Set<K> keySet() {
        return keySetCommand.keySet(contexts.get());
    }
    
    @Override
    public Collection<V> values() {
        return valuesCommand.values(contexts.get());
    }
    
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return entrySetCommand.entrySet(contexts.get());
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public V get(Object key) {
        return getCommand.get(contexts.get(), (K) key);
    }
    
    @Override
    public V put(K key, V value) {
        return putCommand.put(contexts.get(), key, value);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public void putAll(Map<? extends K, ? extends V> map) {
        putCommand.putAll(contexts.get(), (Map<K, V>) map);
    }
    
    @Override
    public V putIfAbsent(K key, V value) {
        return putCommand.putIfAbsent(contexts.get(), key, value);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public V remove(Object key) {
        return removeCommand.remove(contexts.get(), (K) key);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object key, Object value) {
        return removeCommand.remove(contexts.get(), (K) key, (V) value);
    }
    
    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        return replaceCommand.replace(contexts.get(), key, oldValue, newValue);
    }
    
    @Override
    public V replace(K key, V value) {
        return replaceCommand.replace(contexts.get(), key, value);
    }
    
    public SizeCommand<C> getSizeCommand() {
        return sizeCommand;
    }
    
    public void setSizeCommand(SizeCommand<C> sizeCommand) {
        this.sizeCommand = sizeCommand;
    }
    
    public IsEmptyCommand<C> getIsEmptyCommand() {
        return isEmptyCommand;
    }
    
    public void setIsEmptyCommand(IsEmptyCommand<C> isEmptyCommand) {
        this.isEmptyCommand = isEmptyCommand;
    }
    
    public ContainsCommand<C, K> getContainsKeyCommand() {
        return containsKeyCommand;
    }
    
    public void setContainsKeyCommand(ContainsCommand<C, K> containsKeyCommand) {
        this.containsKeyCommand = containsKeyCommand;
    }
    
    public ContainsCommand<C, V> getContainsValueCommand() {
        return containsValueCommand;
    }
    
    public void setContainsValueCommand(ContainsCommand<C, V> containsValueCommand) {
        this.containsValueCommand = containsValueCommand;
    }
    
    public ClearCommand<C> getClearCommand() {
        return clearCommand;
    }
    
    public void setClearCommand(ClearCommand<C> clearCommand) {
        this.clearCommand = clearCommand;
    }
    
    public MapKeySetCommand<C, K> getKeySetCommand() {
        return keySetCommand;
    }
    
    public void setKeySetCommand(MapKeySetCommand<C, K> keySetCommand) {
        this.keySetCommand = keySetCommand;
    }
    
    public MapValuesCommand<C, V> getValuesCommand() {
        return valuesCommand;
    }
    
    public void setValuesCommand(MapValuesCommand<C, V> valuesCommand) {
        this.valuesCommand = valuesCommand;
    }
    
    public MapEntrySetCommand<C, K, V> getEntrySetCommand() {
        return entrySetCommand;
    }
    
    public void setEntrySetCommand(MapEntrySetCommand<C, K, V> entrySetCommand) {
        this.entrySetCommand = entrySetCommand;
    }
    
    public MapGetCommand<C, K, V> getGetCommand() {
        return getCommand;
    }
    
    public void setGetCommand(MapGetCommand<C, K, V> getCommand) {
        this.getCommand = getCommand;
    }
    
    public MapPutCommand<C, K, V> getPutCommand() {
        return putCommand;
    }
    
    public void setPutCommand(MapPutCommand<C, K, V> putCommand) {
        this.putCommand = putCommand;
    }
    
    public MapRemoveCommand<C, K, V> getRemoveCommand() {
        return removeCommand;
    }
    
    public void setRemoveCommand(MapRemoveCommand<C, K, V> removeCommand) {
        this.removeCommand = removeCommand;
    }
    
    public MapReplaceCommand<C, K, V> getReplaceCommand() {
        return replaceCommand;
    }
    
    public void setReplaceCommand(MapReplaceCommand<C, K, V> replaceCommand) {
        this.replaceCommand = replaceCommand;
    }
    
}
