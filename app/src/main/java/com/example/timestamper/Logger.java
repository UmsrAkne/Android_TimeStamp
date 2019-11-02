package com.example.timestamper;

import android.util.Log;

public final class Logger {

    public static void trace(String message){

        //  getStackTrace　を使用してこのメソッドの呼び出し元のクラス名とメソッド名を取得
        //  出力の内容に含める
        StackTraceElement[] ste = new Throwable().getStackTrace();
        String callerClassName = ste[1].getClassName();
        String callerMethodName = ste[1].getMethodName();
        Log.i(" users output",
                 callerClassName + "." + callerMethodName + "\n" + message);
    }

    public  static  void trace(int message){
        trace(String.valueOf(message));
    }

    public  static  void trace(long message){
        trace(String.valueOf(message));
    }

}