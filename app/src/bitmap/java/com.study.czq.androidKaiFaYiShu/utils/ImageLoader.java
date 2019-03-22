package com.study.czq.androidKaiFaYiShu.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.study.czq.androidKaiFaYiShu.R;
import com.study.czq.androidKaiFaYiShu.disjlrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 介绍了Bitmap的高效加载方式、LruCache以及DiskLruCache,现在
 * 我们来着手实现一个优秀的ImageLoader。
 * 一般来说,一个优秀的ImageLoader应该具备如下功能:
 * 1.图片的同步加载;
 * 2.图片的异步加载;
 * 3.图片压缩;
 * 4.内存缓存;
 * 5.磁盘缓存;
 * 6.网络拉取。
 */
public class ImageLoader {
    private static final String TAG = "ImageLoader";
    public static final int MESSAGE_POST_RESULT = 1;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final long KEEP_ALIVE = 10L;
    private static final int TAG_KEY_URI = R.id.imageloader_uri;
    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 50;
    private static final int IO_BUFFER_SIZE = 8 * 1024;
    private static final int DISK_CACHE_INDEX = 0;
    private boolean mIsDiskLruCacheCreated = false;
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);
        public Thread newThread(Runnable r) {
            return new Thread(r,"ImageLoader#" + mCount.getAndIncrement());
        }
    };
    //线程池
    public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
                    CORE_POOL_SIZE,MAXIMUM_POOL_SIZE,
                    KEEP_ALIVE,TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(),sThreadFactory);

    /**
     * 主线程改变ui
     */
    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            LoaderResult result = (LoaderResult) msg.obj;
            ImageView imageView = result.imageView;
            imageView.setImageBitmap(result.bitmap);
            String uri = (String) imageView.getTag(TAG_KEY_URI);
            if (uri.equals(result.uri)) {
                imageView.setImageBitmap(result.bitmap);
            } else {
                Trace.w(TAG,"set image bitmap,but url has changed,ignored!");
            }
        }
    };
    private Context mContext;
    private ImageResizer mImageResizer = new ImageResizer();
    private LruCache<String,Bitmap> mMemoryCache;
    private DiskLruCache mDiskLruCache;


    private ImageLoader(Context context) {
        mContext = context.getApplicationContext();
        //获取最大内存值
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        //设置缓存为最大内存的1/8
        int cacheSize = maxMemory / 8;
        //创建图片缓存类
        mMemoryCache = new LruCache<String,Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key,Bitmap bitmap) {
                //计算图片大小
                return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
            }
        };

        File diskCacheDir = getDiskCacheDir(mContext,"bitmap");
        if (!diskCacheDir.exists()) {
            diskCacheDir.mkdirs();
        }

        //磁盘缓存的容量为50MB。
        if (getUsableSpace(diskCacheDir) > DISK_CACHE_SIZE) {
            try {
                //创建磁盘缓存类
                mDiskLruCache = DiskLruCache.open(diskCacheDir,1,1,
                        DISK_CACHE_SIZE);
                mIsDiskLruCacheCreated = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 构建ImageLoader对象
     * @param context
     * @return
     */
    public static ImageLoader build(Context context) {
        return new ImageLoader(context);
    }

    /**
     * 添加bitmap对象到缓存中
     * @param key
     * @param bitmap
     */
    private void addBitmapToMemoryCache(String key,Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key,bitmap);
        }
    }

    /**
     * 获得缓存的bitmap对象
     * @param key
     * @return
     */
    private Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    /**
     *
     * @param uri
     * @param imageView
     */
    public void bindBitmap(final String uri,final ImageView imageView) {
        bindBitmap(uri,imageView,0,0);
    }

    /**
     *
     * @param uri
     * @param imageView
     * @param reqWidth
     * @param reqHeight
     */
    public void bindBitmap(final String uri,final ImageView imageView,
                           final int reqWidth,final int reqHeight) {
        imageView.setTag(TAG_KEY_URI,uri);
        Bitmap bitmap = loadBitmapFromMemCache(uri);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            return;
        }
        Runnable loadBitmapTask = new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = loadBitmap(uri,reqWidth,reqHeight);
                if (bitmap != null) {
                    LoaderResult result = new LoaderResult(imageView,uri,bitmap);
                    mMainHandler.obtainMessage(MESSAGE_POST_RESULT,result).
                            sendToTarget();
                }
            }
        };
        THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
    }

    /**
     * 加载url网络图片
     *
     * @param uri http url
     * @param reqWidth the width ImageView desired
     * @param reqHeight the height ImageView desired
     * @return bitmap,maybe null.
     */
    public Bitmap loadBitmap(String uri,int reqWidth,int reqHeight) {
        //内存缓存中寻找bitmap
        Bitmap bitmap = loadBitmapFromMemCache(uri);
        if (bitmap != null) {
            Trace.d(TAG,"loadBitmapFromMemCache,url:" + uri);
            return bitmap;
        }
        try {
            //磁盘缓存中寻找bitmap
            bitmap = loadBitmapFromDiskCache(uri,reqWidth,reqHeight);
            if (bitmap != null) {
                Trace.d(TAG,"loadBitmapFromDisk,url:" + uri);
                return bitmap;
            }
            bitmap = loadBitmapFromHttp(uri,reqWidth,reqHeight);
            Trace.d(TAG,"loadBitmapFromHttp,url:" + uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bitmap == null && !mIsDiskLruCacheCreated) {
            Trace.w(TAG,"encounter error,DiskLruCache is not created.");
            bitmap = downloadBitmapFromUrl(uri);
        }
        return bitmap;
    }

    /**
     * 从内存缓存中获取网络图片
     * @param url
     * @return
     */
    private Bitmap loadBitmapFromMemCache(String url) {
        final String key = hashKeyFormUrl(url);
        Bitmap bitmap = getBitmapFromMemCache(key);
        return bitmap;
    }

    /**
     * 加载网络图片
     * @param url
     * @param reqWidth
     * @param reqHeight
     * @return
     * @throws IOException
     */
    private Bitmap loadBitmapFromHttp(String url,int reqWidth,int
            reqHeight)
            throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("can not visit network from UI Thread.");
        }
        if (mDiskLruCache == null) {
            return null;
        }
        String key = hashKeyFormUrl(url);
        DiskLruCache.Editor editor = mDiskLruCache.edit(key);
        if (editor != null) {
            OutputStream outputStream = editor.newOutputStream(DISK_CACHE_INDEX);
            //Editor提供了
            //commit和abort方法来提交和撤销对文件系统的写操作
            if (downloadUrlToStream(url,outputStream)) {
                editor.commit();
            } else {
                editor.abort();
            }
            mDiskLruCache.flush();
        }
        return loadBitmapFromDiskCache(url,reqWidth,reqHeight);
    }

    /**
     * 读取磁盘中的图片缓存，并将图片添加到内存缓存中
     * @param url
     * @param reqWidth
     * @param reqHeight
     * @return
     * @throws IOException
     */
    private Bitmap loadBitmapFromDiskCache(String url,int reqWidth,
                                           int reqHeight) throws IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Trace.w(TAG,"load bitmap from UI Thread,it's not recommended!");
        }
        if (mDiskLruCache == null) {
            return null;
        }
        Bitmap bitmap = null;
        String key = hashKeyFormUrl(url);
        DiskLruCache.Snapshot snapShot = mDiskLruCache.get(key);
        if (snapShot != null) {
            FileInputStream fileInputStream = (FileInputStream)snapShot.
                    getInputStream(DISK_CACHE_INDEX);
            FileDescriptor fileDescriptor = fileInputStream.getFD();
            bitmap = mImageResizer.decodeSampledBitmapFromFileDescriptor
                    (fileDescriptor,reqWidth,reqHeight);
            if (bitmap != null) {
                //将bitmap添加到缓存中
                addBitmapToMemoryCache(key,bitmap);
            }
        }
        return bitmap;
    }

    /**
     * 下载网络图片
     * @param urlString
     * @param outputStream
     * @return
     */
    public boolean downloadUrlToStream(String urlString,OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(),
                    IO_BUFFER_SIZE);
            out = new BufferedOutputStream(outputStream,IO_BUFFER_SIZE);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (IOException e) {
            Trace.e(TAG,"downloadBitmap failed." + e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            CloseableUtil.close(out);
            CloseableUtil.close(in);
        }
        return false;
    }

    /**
     * 直接加载网络图片，不进行缓存
     * @param urlString
     * @return
     */
    private Bitmap downloadBitmapFromUrl(String urlString) {
        Bitmap bitmap = null;
        HttpURLConnection urlConnection = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(),
                    IO_BUFFER_SIZE);
            bitmap = BitmapFactory.decodeStream(in);
        } catch (final IOException e) {
            Trace.e(TAG,"Error in downloadBitmap: " + e);} finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            CloseableUtil.close(in);
        }
        return bitmap;
    }

    /**
     * 获取url的md5值
     * @param url
     * @return
     */
    private String hashKeyFormUrl(String url) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(url.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }
    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 创建磁盘缓存路径
     * @param context
     * @param uniqueName
     * @return
     */
    public File getDiskCacheDir(Context context,String uniqueName) {
        //Environment.MEDIA_MOUNTED表明对象是否存在并具有读/写权限
        //getExternalStorageState() 方法，返回String 获取外部存储设备的当前状态。
        boolean externalStorageAvailable =
                Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        final String cachePath;
        if (externalStorageAvailable) {
            //获取路径：/storage/emulated/0/Android/data/应用包名/cache
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            //获取路径：/data/user/0/应用包名/cache
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 获取磁盘剩余控件
     * @param path
     * @return
     */
    private long getUsableSpace(File path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return path.getUsableSpace();
        }
        final StatFs stats = new StatFs(path.getPath());
        return (long) stats.getBlockSize() * (long) stats.getAvailableBlocks();
    }


    private static class LoaderResult {
        public ImageView imageView;
        public String uri;
        public Bitmap bitmap;
        public LoaderResult(ImageView imageView,String uri,Bitmap bitmap) {
            this.imageView = imageView;
            this.uri = uri;
            this.bitmap = bitmap;
        }
    }
}

