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

import jc.command.AddCommand;
import jc.command.ClearCommand;
import jc.command.ContainsCommand;
import jc.command.IsEmptyCommand;
import jc.command.IteratorCommand;
import jc.command.RemoveCommand;
import jc.command.SizeCommand;


public abstract class ContextCollection<C extends CollectionContext<T>, T> implements Collection<T> {
    
    protected SizeCommand<C> sizeCommand;
    protected IsEmptyCommand<C> isEmptyCommand;
    protected ContainsCommand<C, T> containsCommand;
    protected ClearCommand<C> clearCommand;
    protected IteratorCommand<C, T> iteratorCommand;
    protected RemoveCommand<C, T> removeCommand;
    protected AddCommand<C, T> addCommand;
    protected ThreadLocal<C> contexts;
    
    public ContextCollection() {
        contexts = getContexts();
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
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        return containsCommand.contains(contexts.get(), (T) o);
    }
    
    @Override
    public CloseableIterator<T> iterator() {
        return iteratorCommand.iterator(contexts.get());
    }
    
    @Override
    public void clear() {
        clearCommand.clear(contexts.get());
    }
    
    @Override
    public boolean add(T e) {
        return addCommand.add(contexts.get(), e);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        return removeCommand.remove(contexts.get(), (T) o);
    }
    
    @Override
    public boolean addAll(Collection<? extends T> c) {
        return addCommand.addAll(contexts.get(), c);
    }
    
    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public <V> V[] toArray(V[] a) {
        throw new UnsupportedOperationException();
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
    
    public ContainsCommand<C, T> getContainsCommand() {
        return containsCommand;
    }
    
    public void setContainsCommand(ContainsCommand<C, T> containsCommand) {
        this.containsCommand = containsCommand;
    }
    
    public ClearCommand<C> getClearCommand() {
        return clearCommand;
    }
    
    public void setClearCommand(ClearCommand<C> clearCommand) {
        this.clearCommand = clearCommand;
    }
    
    public IteratorCommand<C, T> getIteratorCommand() {
        return iteratorCommand;
    }
    
    public void setIteratorCommand(IteratorCommand<C, T> iteratorCommand) {
        this.iteratorCommand = iteratorCommand;
    }
    
    public RemoveCommand<C, T> getRemoveCommand() {
        return removeCommand;
    }
    
    public void setRemoveCommand(RemoveCommand<C, T> removeCommand) {
        this.removeCommand = removeCommand;
    }
    
    public AddCommand<C, T> getAddCommand() {
        return addCommand;
    }
    
    public void setAddCommand(AddCommand<C, T> addCommand) {
        this.addCommand = addCommand;
    }
    
}
