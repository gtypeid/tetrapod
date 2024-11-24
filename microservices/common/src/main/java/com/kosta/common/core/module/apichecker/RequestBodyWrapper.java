package com.kosta.common.core.module.apichecker;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class RequestBodyWrapper extends HttpServletRequestWrapper {
    private byte[] bytes;
    private String requestBody;

    public RequestBodyWrapper(HttpServletRequest request) throws IOException {
        super(request);

        InputStream in = super.getInputStream();
        bytes = IOUtils.toByteArray(in);
        requestBody = new String(bytes);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        return  new ServletImpl(bis);
    }

    public String getRequestBody() {
        return this.requestBody;
    }

    class ServletImpl extends ServletInputStream{
        private InputStream is;
        public ServletImpl(InputStream bis){
            is = bis;
        }

        @Override
        public int read() throws IOException {
            return is.read();
        }

        @Override
        public int read(byte[] b) throws IOException {
            return is.read(b);
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setReadListener(ReadListener readListener) {

        }
    }
}