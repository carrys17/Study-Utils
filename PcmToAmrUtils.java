package com.example.shang.study_utils;

import android.media.AmrInputStream;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by admin on 2018/1/16.
 */


// 利用android.media.AmrInputStream将Pcm文件转换为Amr文件
public class PcmToAmrUtils {


        private static final String TAG = "Module";

        public static void pcm2Amr(String pcmPath , String amrPath) throws IOException {

            Log.i(TAG, "pcmPath: "+pcmPath);
            Log.i(TAG, "amrPath: "+amrPath);

            FileInputStream fis = new FileInputStream(pcmPath);
            AmrInputStream ais = new AmrInputStream(fis);
            OutputStream out = new FileOutputStream(amrPath);
            byte[] buf = new byte[4096];
            int len = -1;
			/*
			 * 下面的amr的文件头
			 * 缺少这几个字节是不行的
			 */
            out.write(0x23);
            out.write(0x21);
            out.write(0x41);
            out.write(0x4D);
            out.write(0x52);
            out.write(0x0A);
            while((len = ais.read(buf)) >0){
                out.write(buf,0,len);
            }
            out.close();
            ais.close();

    }

}
