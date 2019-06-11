package com.example.clientversion1.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

public class ResponseWrapper extends HttpServletResponseWrapper {

    private ByteArrayOutputStream buffer;

    private ServletOutputStream out;

    private PrintWriter pw;

    public ResponseWrapper(HttpServletResponse response) throws IOException {
        super(response);
        buffer = new ByteArrayOutputStream();
        out = getOutputStream();
        pw = new PrintWriter(new OutputStreamWriter(buffer,this.getCharacterEncoding()));
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if(out == null) {
            out = new ServletOutputStream() {
                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setWriteListener(WriteListener writeListener) {

                }

                @Override
                public void write(int b) throws IOException {
                    buffer.write(b);
                }
            };
        }
        return out;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return pw;
    }

    @Override
    public void flushBuffer() throws IOException {
        if(out != null)
            out.flush();
        if(pw != null)
            pw.flush();
    }

    @Override
    public void reset() {
        buffer.reset();
    }

    public String getData() throws IOException {
        return new String(buffer.toByteArray());
    }
}
