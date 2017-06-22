package com.example.jiewu.calculator;

import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testParserApk() throws IOException {
        String path = System.getProperty("user.dir") + "/app/src/test/java/com/example/jiewu/calculator//";

        String apkPath = path + "2017-05-10_smartpos_v1.0.0_20170510_debug.apk";

        ApkFile file = new ApkFile(apkPath);
        ApkMeta apkMeta = file.getApkMeta();
        System.out.println(apkMeta.toString());
    }
}