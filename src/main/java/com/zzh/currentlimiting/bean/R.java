package com.zzh.currentlimiting.bean;

import java.util.HashMap;

/**
 * @param
 * @function R中的静态方法可以直接调用。如R.OK（）
 * @return
 */
public class R extends HashMap<String, Object> {
    public static R ok() {
        R r = new R();
        r.put("code", 20000);
        r.put("msg", "请求成功");
        return r;
    }

    public static R ok(String msg, HashMap<String, Object> Data) {
        R r = new R();
        r.put("code", 20000);
        r.put("msg", msg);
        r.put("Data", Data);
        return r;
    }

    public static R ok(String msg) {
        R r = new R();
        r.put("code", 20000);
        r.put("msg", msg);
        return r;
    }

    public static R error() {
        R r = new R();
        r.put("code", 20001);
        r.put("msg", "请求失败");
        return r;
    }

    /**
     * @param
     * @return
     * @function 注意这里的put重写了hashmap中的put方法,
     * hashmap中的put返回值是Object，我们这里是R
     * 这个方法不能删，删了就没有R.ok().put()
     * @override覆盖
     */

    public R put(String key, Object value) {
        super.put(key, value);
        return this;
    }
    /*public <T> R put(String key, HashMap<Integer,T> value) {
        Set<Entry<Integer, T>> entries = value.entrySet();
        for(Map.Entry<Integer, T> t: entries){
            System.out.println(t);
        }
        return this;
    }*/
    /**
     * @param
     * @return
     * @function 设计vueput的原因：由于vue中取值为response.data.code的形式，故将数据存在hashmap较合理,即R中hashmap中
     * R.ok().vuePut("rows","f")原理：ok()中实例化R的同时，为实例化R中的hashmap开辟了内存地址，
     * 故每vuePut一次，都是相当与给ok()中实例化R中的hashmap赋值，结果返回的R对象包括vueput后的hashmap
     */
/*
    public R Vueput(String key, Object value) {
        this.data.put(key, value);
        return this;
    }*/

}
