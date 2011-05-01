/*
 * Copyright 2011 Roger Kapsi
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.ardverk.bloom;

import java.util.BitSet;

public class BloomFilter<E> {
    
    private final HashFunction<? super E> function;
    
    private final BitSet bitSet;
    
    private final int k;
    
    private final int size;
    
    public BloomFilter(double c, int n, int k, 
            HashFunction<? super E> function) {
        this.function = function;
        this.k = k;
        this.size = (int)Math.ceil(c * n);
        this.bitSet = new BitSet(size);
    }
    
    public void add(E element) {
        int[] hashes = function.hash(element, k);
        for (int hash : hashes) {
            bitSet.set(Math.abs(hash % size), true);
        }
    }
    
    public boolean contains(E element) {
        int[] hashes = function.hash(element, k);
        for (int hash : hashes) {
            if (!bitSet.get(Math.abs(hash % size))) {
                return false;
            }
        }
        return true;
    }
    
    public void clear() {
        bitSet.clear();
    }
}
