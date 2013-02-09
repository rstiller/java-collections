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

import java.util.Queue;

import jc.command.QueueElementCommand;
import jc.command.QueueOfferCommand;
import jc.command.QueuePeekCommand;
import jc.command.QueuePollCommand;
import jc.command.QueueRemoveCommand;


public abstract class ContextQueue<C extends CollectionContext<E>, E> extends ContextCollection<C, E> implements
        Queue<E> {
    
    protected QueuePollCommand<C, E> queuePollCommand;
    protected QueueRemoveCommand<C, E> queueRemoveCommand;
    protected QueuePeekCommand<C, E> queuePeekCommand;
    protected QueueOfferCommand<C, E> queueOfferCommand;
    protected QueueElementCommand<C, E> queueElementCommand;
    
    @Override
    public E remove() {
        return queueRemoveCommand.remove(contexts.get());
    }
    
    @Override
    public boolean offer(E e) {
        return queueOfferCommand.offer(contexts.get(), e);
    }
    
    @Override
    public E poll() {
        return queuePollCommand.poll(contexts.get());
    }
    
    @Override
    public E element() {
        return queueElementCommand.element(contexts.get());
    }
    
    @Override
    public E peek() {
        return queuePeekCommand.peek(contexts.get());
    }
    
    public QueuePollCommand<C, E> getQueuePollCommand() {
        return queuePollCommand;
    }
    
    public void setQueuePollCommand(QueuePollCommand<C, E> queuePollCommand) {
        this.queuePollCommand = queuePollCommand;
    }
    
    public QueueRemoveCommand<C, E> getQueueRemoveCommand() {
        return queueRemoveCommand;
    }
    
    public void setQueueRemoveCommand(QueueRemoveCommand<C, E> queueRemoveCommand) {
        this.queueRemoveCommand = queueRemoveCommand;
    }
    
    public QueuePeekCommand<C, E> getQueuePeekCommand() {
        return queuePeekCommand;
    }
    
    public void setQueuePeekCommand(QueuePeekCommand<C, E> queuePeekCommand) {
        this.queuePeekCommand = queuePeekCommand;
    }
    
    public QueueOfferCommand<C, E> getQueueOfferCommand() {
        return queueOfferCommand;
    }
    
    public void setQueueOfferCommand(QueueOfferCommand<C, E> queueOfferCommand) {
        this.queueOfferCommand = queueOfferCommand;
    }
    
    public QueueElementCommand<C, E> getQueueElementCommand() {
        return queueElementCommand;
    }
    
    public void setQueueElementCommand(QueueElementCommand<C, E> queueElementCommand) {
        this.queueElementCommand = queueElementCommand;
    }
    
}
