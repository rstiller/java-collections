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
package jc.mongodb;

import java.util.ArrayList;
import java.util.List;

public class TestBean {
    
    protected String name;
    protected int count;
    protected byte[] data;
    protected TestBean next;
    protected List<TestBean> beans = new ArrayList<TestBean>();
    
    public TestBean() {
    }
    
    public TestBean(String name) {
        this.name = name;
    }
    
    @Override
    public boolean equals(Object obj) {
        return name.equals(((TestBean) obj).name);
    }
    
    public int getCount() {
        return count;
    }
    
    public void setCount(int count) {
        this.count = count;
    }
    
    public TestBean getNext() {
        return next;
    }
    
    public void setNext(TestBean next) {
        this.next = next;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public byte[] getData() {
        return data;
    }
    
    public void setData(byte[] data) {
        this.data = data;
    }
    
    public List<TestBean> getBeans() {
        return beans;
    }
    
    public void setBeans(List<TestBean> beans) {
        this.beans = beans;
    }
    
}
