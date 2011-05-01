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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestHashFunction implements HashFunction<byte[]> {

    public static final String ALGORITHM = "MD5";
    
    private final String algorithm;
    
    public DigestHashFunction() {
        this(ALGORITHM);
    }
    
    public DigestHashFunction(String algorithm) {
        this.algorithm = algorithm;
    }
    
    @Override
    public int[] hash(byte[] element, int k) {
        
        MessageDigest messageDigest = getMessageDigest(algorithm);
        HashStream stream = new HashStream(messageDigest, element);
        
        int[] hashes = new int[k];
        
        for (int i = 0; i < k; i++) {
            hashes[i] = (stream.next() << 24) 
                      | (stream.next() << 16) 
                      | (stream.next() <<  8) 
                      | (stream.next()      );
        }
        
        return hashes;
    }
    
    private static MessageDigest getMessageDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(
                    "algorithm=" + algorithm, e);
        }
    }
    
    private static class HashStream {
        
        private final MessageDigest messageDigest;
        
        private final byte[] element;
        
        private int salt = 0;
        
        private int index = 0;
        
        private byte[] digest;
        
        public HashStream(MessageDigest messageDigest, byte[] element) {
            this.messageDigest = messageDigest;
            this.element = element;
        }
        
        public int next() {
            if (digest == null || index >= digest.length) {
                messageDigest.update((byte)salt++);
                digest = messageDigest.digest(element);
                index = 0;
            }
            
            return digest[index++] & 0xFF;
        }
    }
}