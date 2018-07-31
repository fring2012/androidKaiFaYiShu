// ISecurityCenter.aidl
package com.baidu_map.lhq.permission_demo;

// Declare any non-default types here with import statements

interface ISecurityCenter {
    String encrypt(String content);
    String decrypt(String password);
}
