package com.yixin.garage.xss;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * 用于将字节数组转换为字符串
 * 
 * 
 * 
 */
public class HttpHelper {
    /**
     * 获取请求Body
     * 
     * @param request
     * @return
     */
    public static String getBodyString(ServletRequest request) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = request.getInputStream();
            
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
    
    public static ServletInputStream stringToServletInputStream(String body){
        ServletInputStream inputStream = null;
        if (StringUtils.isNotEmpty(body)) {
            final byte[] myBytes = body.getBytes(StandardCharsets.UTF_8);
                inputStream = new ServletInputStream() {

                    private int lastIndexRetrieved = -1;
                    private ReadListener readListener = null;

                    public boolean isFinished() {
                        return (lastIndexRetrieved == myBytes.length-1);
                    }

                    public boolean isReady() {
                        // This implementation will never block
                        // We also never need to call the readListener from this method, as this method will never return false
                        return isFinished();
                    }

                    public void setReadListener(ReadListener readListener) {
                        this.readListener = readListener;
                        if (!isFinished()) {
                            try {
                                readListener.onDataAvailable();
                            } catch (IOException e) {
                                readListener.onError(e);
                            }
                        } else {
                            try {
                                readListener.onAllDataRead();
                            } catch (IOException e) {
                                readListener.onError(e);
                            }
                        }
                    }

                    @Override
                    public int read() throws IOException {
                        int i;
                        if (!isFinished()) {
                            i = myBytes[lastIndexRetrieved+1];
                            lastIndexRetrieved++;
                            if (isFinished() && (readListener != null)) {
                                try {
                                    readListener.onAllDataRead();
                                } catch (IOException ex) {
                                    readListener.onError(ex);
                                    throw ex;
                                }
                            }
                            return i;
                        } else {
                            return -1;
                        }
                    }
                };
        }
        return inputStream;
    }
}

