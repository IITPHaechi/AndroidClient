package iitp.project.haechi.purdueapps3;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;
import org.jcodec.api.android.FrameGrab;
import org.jcodec.common.SeekableByteChannel;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Created by dnay2 on 2016-10-18.
 */
public class MyJcodecVideoView extends AppCompatActivity {

    private static final String VIDEO_URL = "http://172.24.1.1:8082/";

    private static final int FRAME_NUMBER = 20;

    FrameGrab frameGrab;

    WebSocketClient mWebSocketClient;

    private class HttpChannel implements SeekableByteChannel{
        private static final String LOG_TAG_HTTP_CHANNEL = "HttpChannel";

        private URL url;
        private ReadableByteChannel ch;
        private long pos;
        private long length;

        public HttpChannel(URL url) {
            this.url = url;
        }

        @Override
        public long position() throws IOException {
            return pos;
        }

        @Override
        public SeekableByteChannel position(long newPosition) throws IOException {
            if(newPosition == pos) return this;
            if(ch != null){
                ch.close();
                ch = null;
            }
            pos = newPosition;
            Log.d(LOG_TAG_HTTP_CHANNEL, "Seeking to : "+ newPosition);
            return this;
        }

        @Override
        public long size() throws IOException {
            return length;
        }

        @Override
        public SeekableByteChannel truncate(long size) throws IOException {
            throw new IOException("Truncate on HTTP is not supported.");
        }

        @Override
        public int read(ByteBuffer byteBuffer) throws IOException {
            ensureOpen();
            int read = ch.read(byteBuffer);
            if(read != -1)
                pos += read;
            return read;
        }

        @Override
        public int write(ByteBuffer byteBuffer) throws IOException {
            throw new IOException("Write to HTTP is not supported.");
        }

        @Override
        public boolean isOpen() {
            return ch != null && ch.isOpen();
        }

        @Override
        public void close() throws IOException {
            ch.close();
        }

        private void ensureOpen() throws IOException{
            if(ch == null){
                URLConnection connection = url.openConnection();
                if(pos > 0){
                    connection.addRequestProperty("Range", "bytes=" + pos + "-");
                }
                ch = Channels.newChannel(connection.getInputStream());
                String resp = connection.getHeaderField("Content-Range");
                if(resp != null){
                    Log.d(LOG_TAG_HTTP_CHANNEL, resp);
                    length = Long.parseLong(resp.split("/")[1]);
                } else {
                    resp = connection.getHeaderField("Content-Length");
                    Log.d(LOG_TAG_HTTP_CHANNEL, resp);
                    length = Long.parseLong(resp);
                }
            }
        }
    }

    class DecodeTask extends AsyncTask<URL, Void, Bitmap> {
        private static final String LOG_TAG_DECODE_TASK = "DecodeTask";

        private Exception exception;

        protected Bitmap doInBackground(URL ...url) {
            try {
                if(frameGrab == null)
                    frameGrab = new FrameGrab(new HttpChannel(url[0]));
                return frameGrab.getFrame();
            } catch (Exception e) {
                Log.e(LOG_TAG_DECODE_TASK, "Could not decode one frame.", e);
                this.exception = e;
                return null;
            }
        }

        protected void onPostExecute(Bitmap feed) {
            if(feed != null) {
                new DecodeTask().execute();
                Log.d(LOG_TAG_DECODE_TASK, "Decoded image size: [" + feed.getWidth() + ", " + feed.getHeight() + "].");
                ImageView img = (ImageView) findViewById(R.id.frame);
                img.setImageBitmap(feed);
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jcodec);
        try{
            new DecodeTask().execute(new URL(VIDEO_URL));
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
    }

    private class WebSocketChannel implements SeekableByteChannel{
        private static final String LOG_TAG_WEBSOCKET_CHANNEL = "WebSocket";

        private URL url;
        private ReadableByteChannel ch;
        private long pos;
        private long length;

        public WebSocketChannel(URL url) {
            this.url = url;
        }

        @Override
        public long position() throws IOException {
            return pos;
        }

        @Override
        public SeekableByteChannel position(long newPosition) throws IOException {
            if(newPosition == pos) return this;
            if(ch != null){
                ch.close();
                ch = null;
            }
            pos = newPosition;
            return this;
        }

        @Override
        public long size() throws IOException {
            return length;
        }

        @Override
        public SeekableByteChannel truncate(long size) throws IOException {
            throw new IOException("truncate on websocket is not supported");
        }

        @Override
        public int read(ByteBuffer byteBuffer) throws IOException {
            ensureOpen();
            int read = ch.read(byteBuffer);
            if(read != -1){
                pos += read;
            }
            return read;
        }

        @Override
        public boolean isOpen() {
            return ch != null && ch.isOpen();
        }

        @Override
        public void close() throws IOException {
            ch.close();
        }

        private void ensureOpen() throws IOException{
            URI uri = null;

            try{
                uri = new URI(VIDEO_URL);
            }catch (URISyntaxException e){
                e.printStackTrace();
                return;
            }

            if(ch == null){
                final WebSocketClient client = new WebSocketClient(uri, new Draft_10()) {
                    @Override
                    public void onOpen(ServerHandshake handshakedata) {
                        //메시지 받는 부분

                    }

                    @Override
                    public void onMessage(String message) {
                        //메시지 받기
                    }

                    @Override
                    public void onClose(int code, String reason, boolean remote) {

                    }

                    @Override
                    public void onError(Exception ex) {

                    }
                };

                client.connect();

            }
        }

        @Override
        public int write(ByteBuffer byteBuffer) throws IOException {
            throw new IOException("Write to WebSocket is not supported");
        }
    }
}
