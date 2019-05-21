package cc.wanshan.gis.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CmdUtils {

  private static Logger LOG = LoggerFactory.getLogger(CmdUtils.class);

  /**
   * linux系统执行cmd命令
   *
   * @param cmd 命令行
   * @return
   */
  public static boolean process(String cmd) {
    String[] commands = {"/bin/sh", "-c", cmd};
    return execute(commands);
  }

  /**
   * window系统执行cmd命令
   *
   * @param cmd 命令行
   * @return
   */
  public static boolean run(String cmd) {
    String[] commands = {"cmd", "/C", cmd};
    return execute(commands);
  }

  /**
   * window或Linux执行多条cmd命令
   *
   * @param commands 多条命令行
   * @return
   */
  public static boolean process(String[] commands) {

    return execute(commands);
  }

  private static boolean execute(String[] commands) {
    try {
      final Process process = Runtime.getRuntime().exec(commands);
      printMessage(process.getInputStream(), "Output");
      printMessage(process.getErrorStream(), "Error");
      int exitCode = process.waitFor();
      if (exitCode != 0) {
        LOG.warn("退出码： " + exitCode);
        return false;
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  private static void printMessage(final InputStream ips, String type) {
    new Thread(
            new Runnable() {
              @Override
              public void run() {
                BufferedReader bf = new BufferedReader(new InputStreamReader(ips));
                String line = null;
                try {
                  while ((line = bf.readLine()) != null) {
                    if (type.equals("Error")) {
                      LOG.warn("Error: " + line);
                    } else {
                      LOG.info("Output: " + line);
                    }
                  }
                } catch (IOException e) {
                  e.printStackTrace();
                }
              }
            })
        .start();
  }
}
